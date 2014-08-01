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

import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by akamensky on 7/31/14.
 */
public class FileImpl implements File {

    private Part part;

    public FileImpl(Part part) {
        if (part != null) {
            this.part = part;
        } else {
            throw new RuntimeException("Part cannot be null!");
        }
    }

    private static byte[] streamToBytes(InputStream in) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    public String getFilename() {
        return this.part.getSubmittedFileName();
    }

    @Deprecated
    public String getFilepath() {
        return null;
    }

    public String getMime() {
        return this.part.getContentType();
    }

    public long getSize() {
        return this.part.getSize();
    }

    public InputStream getInputStream() {
        InputStream in = null;
        try {
            in = this.part.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public byte[] getBytes() {
        return streamToBytes(this.getInputStream());
    }

    @Deprecated
    public java.io.File getFile() {
        return null;
    }

    public void delete() {
        try {
            this.part.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
