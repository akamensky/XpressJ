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

import java.util.ArrayList;

/**
 * Created by akamensky on 8/6/14.
 */
class Multipart{
    ArrayList<String> lines = new ArrayList<>();
    public static final String FILENAME_HEADER = " filename=\"";
    public static final String FIELDNAME_HEADER = " name=\"";
    public static final String MIME_TYPE_HEADER = "Content-Type: ";

    public boolean isFileUpload(){
        boolean result = false;
        if (this.lines.size() > 0 && this.lines.get(0).contains(FILENAME_HEADER)) {
            result = true;
        }
        return result;
    }
    public String getFieldName(){
        String result = null;
        if (this.lines.size() > 0 && this.lines.get(0).contains(FIELDNAME_HEADER)) {
            result = this.lines.get(0).substring(this.lines.get(0).indexOf(FIELDNAME_HEADER) + FIELDNAME_HEADER.length(), this.lines.get(0).indexOf("\"", this.lines.get(0).indexOf(FIELDNAME_HEADER) + FIELDNAME_HEADER.length()));
        }
        return result;
    }

    public String getFilename(){
        String result = null;
        if (this.lines.size() > 0 && this.lines.get(0).contains(FILENAME_HEADER)) {
            result = this.lines.get(0).substring(this.lines.get(0).indexOf(FILENAME_HEADER) + FILENAME_HEADER.length(), this.lines.get(0).indexOf("\"", this.lines.get(0).indexOf(FILENAME_HEADER) + FILENAME_HEADER.length()));
        }
        return result;
    }

    public String getContentType(){
        String result = null;
        for (int i = 1; i < this.lines.size(); i++) {
            if (this.lines.get(i).isEmpty()) {
                break;
            } else {
                if (this.lines.get(i).startsWith(MIME_TYPE_HEADER)) {
                    result = this.lines.get(i).substring(MIME_TYPE_HEADER.length());
                }
            }
        }
        return result;
    }
}
