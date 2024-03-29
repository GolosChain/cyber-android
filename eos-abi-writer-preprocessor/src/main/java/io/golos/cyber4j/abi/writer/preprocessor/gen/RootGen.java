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
package io.golos.cyber4j.abi.writer.preprocessor.gen;

import com.google.googlejavaformat.java.FormatterException;
import io.golos.cyber4j.abi.writer.preprocessor.FreeMarker;
import io.golos.cyber4j.abi.writer.preprocessor.SourceFileGenerator;
import io.golos.cyber4j.abi.writer.preprocessor.model.AbiWriterModel;

import java.io.IOException;

import io.golos.cyber4j.abi.writer.preprocessor.FreeMarker;
import io.golos.cyber4j.abi.writer.preprocessor.FreeMarker;

class RootGen extends Gen<RootMap> {

    private final AbiWriterModel abiWriterModel;
    private final RootMap rootMap;

    RootGen(
        AbiWriterModel abiWriterModel,
        RootMap rootMap,
        FreeMarker freeMarker,
        SourceFileGenerator sourceFileGenerator
    ) {

        super(freeMarker, sourceFileGenerator);

        this.abiWriterModel = abiWriterModel;
        this.rootMap = rootMap;
    }

    void write() throws IOException, FormatterException {
        super.write(
            "AbiBinaryGen.template",
            rootMap,
            abiWriterModel.getClassPackage(),
            "AbiBinaryGen" + abiWriterModel.getClassName());
    }
}
