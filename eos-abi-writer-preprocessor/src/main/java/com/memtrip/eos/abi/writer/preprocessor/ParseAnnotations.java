/**
 * Copyright 2013-present memtrip LTD.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memtrip.eos.abi.writer.preprocessor;

import com.memtrip.eos.abi.writer.AccountNameCollectionCompress;
import com.memtrip.eos.abi.writer.AccountNameCompress;
import com.memtrip.eos.abi.writer.AssetCompress;
import com.memtrip.eos.abi.writer.BlockNumCompress;
import com.memtrip.eos.abi.writer.BlockPrefixCompress;
import com.memtrip.eos.abi.writer.BoolCompress;
import com.memtrip.eos.abi.writer.ByteCompress;
import com.memtrip.eos.abi.writer.BytesCompress;
import com.memtrip.eos.abi.writer.ChainIdCompress;
import com.memtrip.eos.abi.writer.CheckSumCompress;
import com.memtrip.eos.abi.writer.ChildCompress;
import com.memtrip.eos.abi.writer.CollectionCompress;
import com.memtrip.eos.abi.writer.CyberNameCollectionCompress;
import com.memtrip.eos.abi.writer.CyberNameCompress;
import com.memtrip.eos.abi.writer.DataCompress;
import com.memtrip.eos.abi.writer.FloatCompress;
import com.memtrip.eos.abi.writer.HexCollectionCompress;
import com.memtrip.eos.abi.writer.IntCompress;
import com.memtrip.eos.abi.writer.InterfaceCollectionCompress;
import com.memtrip.eos.abi.writer.LongCollectionCompress;
import com.memtrip.eos.abi.writer.LongCompress;
import com.memtrip.eos.abi.writer.NameCompress;
import com.memtrip.eos.abi.writer.NullableAssetCompress;
import com.memtrip.eos.abi.writer.NullableShortCompress;
import com.memtrip.eos.abi.writer.NullableStringCompress;
import com.memtrip.eos.abi.writer.PublicKeyCompress;
import com.memtrip.eos.abi.writer.ShortCompress;
import com.memtrip.eos.abi.writer.StringCollectionCompress;
import com.memtrip.eos.abi.writer.StringCompress;
import com.memtrip.eos.abi.writer.SymbolCodeCompress;
import com.memtrip.eos.abi.writer.SymbolCompress;
import com.memtrip.eos.abi.writer.TimestampCompress;
import com.memtrip.eos.abi.writer.VariableUIntCompress;
import com.memtrip.eos.abi.writer.preprocessor.model.AbiModel;
import com.memtrip.eos.abi.writer.preprocessor.model.AbiWriterModel;
import com.memtrip.eos.abi.writer.preprocessor.model.CompressType;
import com.memtrip.eos.abi.writer.preprocessor.model.FieldModel;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

final class ParseAnnotations {

    private final Elements elementUtils;

    ParseAnnotations(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    AbiWriterModel abiWriter(Set<? extends Element> elements) {
        Element element = elements.iterator().next();
        return new AbiWriterModel(
                element.getSimpleName().toString(),
                elementUtils.getPackageOf(element).getQualifiedName().toString()
        );
    }

    List<AbiModel> abi(Set<? extends Element> elements) {

        List<AbiModel> abiModels = new ArrayList<>();

        for (Element element : elements) {
            abiModels.add(new AbiModel(
                    element.getSimpleName().toString(),
                    elementUtils.getPackageOf(element).getQualifiedName().toString(),
                    fields(element)
            ));
        }

        return abiModels;
    }

    private List<FieldModel> fields(Element element) {

        List<FieldModel> fieldModels = new ArrayList<>();

        for (Element child : element.getEnclosedElements()) {
            if (elementHasFieldAnnotation(child)) {
                fieldModels.add(new FieldModel(
                        extractName(child.getSimpleName().toString()),
                        extractClassType(child),
                        extractAnnotationType(child)
                ));
            }
        }

        return fieldModels;
    }

    private String extractName(String name) {
        if (name.contains("$")) {
            return name.split("$")[0];
        } else {
            return name;
        }
    }

    private String extractClassType(Element element) {
        TypeMirror typeMirror = element.asType();
        return typeMirror.toString();
    }

    private CompressType extractAnnotationType(Element element) {
        if (hasAnnotation(element, NameCompress.class)) {
            return CompressType.NAME;
        } else if (hasAnnotation(element, AccountNameCompress.class)) {
            return CompressType.ACCOUNT_NAME;
        } else if (hasAnnotation(element, BlockNumCompress.class)) {
            return CompressType.BLOCK_NUM;
        } else if (hasAnnotation(element, BlockPrefixCompress.class)) {
            return CompressType.BLOCK_PREFIX;
        } else if (hasAnnotation(element, PublicKeyCompress.class)) {
            return CompressType.PUBLIC_KEY;
        } else if (hasAnnotation(element, AssetCompress.class)) {
            return CompressType.ASSET;
        } else if (hasAnnotation(element, ChainIdCompress.class)) {
            return CompressType.CHAIN_ID;
        } else if (hasAnnotation(element, HexCollectionCompress.class)) {
            return CompressType.HEX_COLLECTION;
        } else if (hasAnnotation(element, DataCompress.class)) {
            return CompressType.DATA;
        } else if (hasAnnotation(element, TimestampCompress.class)) {
            return CompressType.TIMESTAMP;
        } else if (hasAnnotation(element, ByteCompress.class)) {
            return CompressType.BYTE;
        } else if (hasAnnotation(element, BoolCompress.class)) {
            return CompressType.BOOL;
        } else if (hasAnnotation(element, ShortCompress.class)) {
            return CompressType.SHORT;
        } else if (hasAnnotation(element, IntCompress.class)) {
            return CompressType.INT;
        } else if (hasAnnotation(element, VariableUIntCompress.class)) {
            return CompressType.VARIABLE_UINT;
        } else if (hasAnnotation(element, LongCompress.class)) {
            return CompressType.LONG;
        } else if (hasAnnotation(element, FloatCompress.class)) {
            return CompressType.FLOAT;
        } else if (hasAnnotation(element, BytesCompress.class)) {
            return CompressType.BYTES;
        } else if (hasAnnotation(element, StringCompress.class)) {
            return CompressType.STRING;
        } else if (hasAnnotation(element, StringCollectionCompress.class)) {
            return CompressType.STRING_COLLECTION;
        } else if (hasAnnotation(element, CollectionCompress.class)) {
            return CompressType.COLLECTION;
        } else if (hasAnnotation(element, AccountNameCollectionCompress.class)) {
            return CompressType.ACCOUNT_NAME_COLLECTION;
        } else if (hasAnnotation(element, ChildCompress.class)) {
            return CompressType.CHILD;
        } else if (hasAnnotation(element, LongCollectionCompress.class)) {
            return CompressType.LONG_COLLECTION;
        } else if (hasAnnotation(element, NullableStringCompress.class)) {
            return CompressType.NULLABLE_STRING;
        } else if (hasAnnotation(element, SymbolCodeCompress.class)) {
            return CompressType.SYMBOL_CODE;
        } else if (hasAnnotation(element, SymbolCompress.class)) {
            return CompressType.SYMBOL;
        } else if (hasAnnotation(element, NullableShortCompress.class)) {
            return CompressType.NULLABLE_SHORT;
        } else if (hasAnnotation(element, CheckSumCompress.class)) {
            return CompressType.CHECK_SUM_256;
        } else if (hasAnnotation(element, CyberNameCollectionCompress.class)) {
            return CompressType.CYBER_NAME_COLLECTION;
        } else if (hasAnnotation(element, CyberNameCompress.class)) {
            return CompressType.NAME;
        } else if (hasAnnotation(element, NullableAssetCompress.class)) {
            return CompressType.NULLABLE_ASSET;
        }
        else if (hasAnnotation(element, InterfaceCollectionCompress.class)) {
            return CompressType.INTERFACE_COLLECTION;
        }else {
            throw new IllegalStateException("this method is not covering all the values " +
                    "allowed by elementHasFieldAnnotation. This method is broken!");
        }
    }

    private boolean elementHasFieldAnnotation(Element element) {
        return hasAnnotation(element, NameCompress.class)
                || hasAnnotation(element, AccountNameCompress.class)
                || hasAnnotation(element, BlockNumCompress.class)
                || hasAnnotation(element, BlockPrefixCompress.class)
                || hasAnnotation(element, PublicKeyCompress.class)
                || hasAnnotation(element, AssetCompress.class)
                || hasAnnotation(element, InterfaceCollectionCompress.class)
                || hasAnnotation(element, ChainIdCompress.class)
                || hasAnnotation(element, HexCollectionCompress.class)
                || hasAnnotation(element, DataCompress.class)
                || hasAnnotation(element, TimestampCompress.class)
                || hasAnnotation(element, CheckSumCompress.class)
                || hasAnnotation(element, ByteCompress.class)
                || hasAnnotation(element, ShortCompress.class)
                || hasAnnotation(element, IntCompress.class)
                || hasAnnotation(element, VariableUIntCompress.class)
                || hasAnnotation(element, LongCompress.class)
                || hasAnnotation(element, FloatCompress.class)
                || hasAnnotation(element, BytesCompress.class)
                || hasAnnotation(element, CyberNameCompress.class)
                || hasAnnotation(element, StringCompress.class)
                || hasAnnotation(element, StringCollectionCompress.class)
                || hasAnnotation(element, CollectionCompress.class)
                || hasAnnotation(element, AccountNameCollectionCompress.class)
                || hasAnnotation(element, ChildCompress.class)
                || hasAnnotation(element, BoolCompress.class)
                || hasAnnotation(element, CyberNameCollectionCompress.class)
                || hasAnnotation(element, NullableShortCompress.class)
                || hasAnnotation(element, NullableStringCompress.class)
                || hasAnnotation(element, SymbolCompress.class)
                || hasAnnotation(element, SymbolCodeCompress.class)
                || hasAnnotation(element, NullableAssetCompress.class)
                || hasAnnotation(element, LongCollectionCompress.class);
    }

    private boolean hasAnnotation(Element element, Class<? extends Annotation> clazz) {
        return element.getAnnotation(clazz) != null;
    }
}
