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

package xpressj.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by akamensky on 8/14/14.
 */
public class FreemarkerTemplate implements Template {
    private boolean useExternalTemplates;
    private String templateLocation;
    private TemplateConfiguration configuration;
    private Configuration cfg;

    public void initialize(TemplateConfiguration configuration) {
        this.cfg = new Configuration();
        this.configuration = configuration;
        this.templateLocation = this.configuration.getExternalTemplateLocation();
        if (this.templateLocation == null) {
            this.useExternalTemplates = false;
            this.templateLocation = this.configuration.getTemplateLocation();
        } else {
            this.useExternalTemplates = true;
        }
        if (!this.templateLocation.startsWith("/")) {
            throw new IllegalArgumentException("Invalid location value. Location should start with \"/\" character");
        }
        if (this.useExternalTemplates) {
            File dir = new File(this.templateLocation);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IllegalArgumentException("Invalid location value. Path " + this.templateLocation + " does not exist.");
            }
            try {
                cfg.setDirectoryForTemplateLoading(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cfg.setClassForTemplateLoading(FreemarkerTemplate.class, this.templateLocation);
        }
    }

    public String process(String templateName, Object obj) {
        try {
            //Load template
            freemarker.template.Template template = this.cfg.getTemplate(templateName);

            //process template
            Writer result = new StringWriter();
            template.process(obj, result);

            return result.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
