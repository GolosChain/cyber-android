/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.golos.cyber4j.abi.writer.preprocessor.template;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractListObjectTemplateMethod implements TemplateMethodModelEx {

    public static Map<String, Object> mapEntry() {
        Map<String, Object> map = new HashMap<>();
        map.put("extractListObject", new ExtractListObjectTemplateMethod());
        return map;
    }

    private String extractPackageType(String arg) {
        if (arg.contains("<")) {
            return arg.split("<")[1].replace(">","");
        }

        throw new IllegalStateException("Unexpected list format.");
    }

    private String extraClassName(String packageType) {
        String[] parts = packageType.split("\\.");
        return parts[parts.length - 1];
    }

    private String typeArg(List args) {
        if (!args.isEmpty() && args.get(0) instanceof SimpleScalar) {
            return ((SimpleScalar) args.get(0)).getAsString();
        } else {
            throw new IllegalStateException("The extractListObject(String type); method requires one string argument.");
        }
    }

    @Override
    public Object exec(List arguments) {
        return extraClassName(extractPackageType(typeArg(arguments)));
    }
}