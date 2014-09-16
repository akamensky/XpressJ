/*
 * Copyright 2014 - Alexey Kamenskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xpressj.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.io.WriterOutputStream;
import org.eclipse.jetty.server.HttpOutput;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.Resource;

/**
 * Created by akamensky on 9/16/14.
 */
public class JettyResourceHandler extends ResourceHandler {
    private static final Logger LOG = Log.getLogger(ResourceHandler.class);

    ContextHandler _context;
    Resource _baseResource;
    Resource _defaultStylesheet;
    Resource _stylesheet;
    String[] _welcomeFiles={"index.html"};
    MimeTypes _mimeTypes = new MimeTypes();
    String _cacheControl;
    boolean _directory;
    boolean _etags;
    int _minMemoryMappedContentLength=-1;
    int _minAsyncContentLength=0;

    @Override
    public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (baseRequest.isHandled())
            return;

        boolean skipContentBody = false;

        if(!HttpMethod.GET.is(request.getMethod()))
        {
            if(!HttpMethod.HEAD.is(request.getMethod()))
            {
                //try another handler
                super.handle(target, baseRequest, request, response);
                return;
            }
            skipContentBody = true;
        }

        Resource resource = getResource(request);
        // If resource is not found
        if (resource==null || !resource.exists())
        {
            // inject the jetty-dir.css file if it matches
            if (target.endsWith("/jetty-dir.css"))
            {
                resource = getStylesheet();
                if (resource==null)
                    return;
                response.setContentType("text/css");
            }
            else
            {
                //no resource - try other handlers
                super.handle(target, baseRequest, request, response);
                return;
            }
        }

        // We are going to serve something
        baseRequest.setHandled(true);

        // handle directories
        if (resource.isDirectory())
        {
            baseRequest.setHandled(false);
            return;
        }

        // Handle ETAGS
        long last_modified=resource.lastModified();
        String etag=null;
        if (_etags)
        {
            // simple handling of only a single etag
            String ifnm = request.getHeader(HttpHeader.IF_NONE_MATCH.asString());
            etag=resource.getWeakETag();
            if (ifnm!=null && resource!=null && ifnm.equals(etag))
            {
                response.setStatus(HttpStatus.NOT_MODIFIED_304);
                baseRequest.getResponse().getHttpFields().put(HttpHeader.ETAG,etag);
                return;
            }
        }

        // Handle if modified since
        if (last_modified>0)
        {
            long if_modified=request.getDateHeader(HttpHeader.IF_MODIFIED_SINCE.asString());
            if (if_modified>0 && last_modified/1000<=if_modified/1000)
            {
                response.setStatus(HttpStatus.NOT_MODIFIED_304);
                return;
            }
        }

        // set the headers
        String mime=_mimeTypes.getMimeByExtension(resource.toString());
        if (mime==null)
            mime=_mimeTypes.getMimeByExtension(request.getPathInfo());
        doResponseHeaders(response,resource,mime);
        if (_etags)
            baseRequest.getResponse().getHttpFields().put(HttpHeader.ETAG,etag);

        if(skipContentBody)
            return;


        // Send the content
        OutputStream out =null;
        try {out = response.getOutputStream();}
        catch(IllegalStateException e) {out = new WriterOutputStream(response.getWriter());}

        // Has the output been wrapped
        if (!(out instanceof HttpOutput))
            // Write content via wrapped output
            resource.writeTo(out,0,resource.length());
        else
        {
            // select async by size
            int min_async_size=_minAsyncContentLength==0?response.getBufferSize():_minAsyncContentLength;

            if (request.isAsyncSupported() &&
                    min_async_size>0 &&
                    resource.length()>=min_async_size)
            {
                final AsyncContext async = request.startAsync();
                Callback callback = new Callback()
                {
                    @Override
                    public void succeeded()
                    {
                        async.complete();
                    }

                    @Override
                    public void failed(Throwable x)
                    {
                        LOG.warn(x.toString());
                        LOG.debug(x);
                        async.complete();
                    }
                };

                // Can we use a memory mapped file?
                if (_minMemoryMappedContentLength>0 &&
                        resource.length()>_minMemoryMappedContentLength &&
                        resource instanceof FileResource)
                {
                    ByteBuffer buffer = BufferUtil.toMappedBuffer(resource.getFile());
                    ((HttpOutput)out).sendContent(buffer,callback);
                }
                else  // Do a blocking write of a channel (if available) or input stream
                {
                    // Close of the channel/inputstream is done by the async sendContent
                    ReadableByteChannel channel= resource.getReadableByteChannel();
                    if (channel!=null)
                        ((HttpOutput)out).sendContent(channel,callback);
                    else
                        ((HttpOutput)out).sendContent(resource.getInputStream(),callback);
                }
            }
            else
            {
                // Can we use a memory mapped file?
                if (_minMemoryMappedContentLength>0 &&
                        resource.length()>_minMemoryMappedContentLength &&
                        resource instanceof FileResource)
                {
                    ByteBuffer buffer = BufferUtil.toMappedBuffer(resource.getFile());
                    ((HttpOutput)out).sendContent(buffer);
                }
                else  // Do a blocking write of a channel (if available) or input stream
                {
                    ReadableByteChannel channel= resource.getReadableByteChannel();
                    if (channel!=null)
                        ((HttpOutput)out).sendContent(channel);
                    else
                        ((HttpOutput)out).sendContent(resource.getInputStream());
                }
            }
        }
    }
}
