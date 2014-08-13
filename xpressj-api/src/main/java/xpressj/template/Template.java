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

/**
 * Created by akamensky on 8/13/14.
 */
public interface Template {
    /**
     * Initialize template engine with appropriate configuration
     * @param configuration
     */
    public void initialize(TemplateConfiguration configuration);

    /**
     * Process template that is loaded from location passed from configuration and return it as String object
     * @param templateName Name of template file
     * @param obj Data object
     * @return String representation of processed template
     */
    public String process(String templateName, Object obj);
}
