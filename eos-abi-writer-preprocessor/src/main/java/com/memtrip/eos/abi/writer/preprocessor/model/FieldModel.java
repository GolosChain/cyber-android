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
package com.memtrip.eos.abi.writer.preprocessor.model;

public class FieldModel {

    private final String name;
    private final String classType;
    private final CompressType compressType;

    public String getName() {
        return name;
    }

    public String getClassType() {
        return classType;
    }

    public boolean isName() {
        return compressType == CompressType.NAME;
    }

    public boolean isAccountName() {
        return compressType == CompressType.ACCOUNT_NAME;
    }

    public boolean isBlockNum() {
        return compressType == CompressType.BLOCK_NUM;
    }

    public boolean isBlockPrefix() {
        return compressType == CompressType.BLOCK_PREFIX;
    }

    public boolean isPublicKey() {
        return compressType == CompressType.PUBLIC_KEY;
    }

    public boolean isAsset() {
        return compressType == CompressType.ASSET;
    }

    public boolean isNullableAsset() {
        return compressType == CompressType.NULLABLE_ASSET;
    }

    public boolean isChainId() {
        return compressType == CompressType.CHAIN_ID;
    }

    public boolean isHexCollection() {
        return compressType == CompressType.HEX_COLLECTION;
    }

    public boolean isData() {
        return compressType == CompressType.DATA;
    }

    public boolean isTimestamp() {
        return compressType == CompressType.TIMESTAMP;
    }

    public boolean isInterfaceCollection() {
        return compressType == CompressType.INTERFACE_COLLECTION;
    }

    public boolean isByte() {
        return compressType == CompressType.BYTE;
    }

    public boolean isShort() {
        return compressType == CompressType.SHORT;
    }

    public boolean isCheckSum() {
        return compressType == CompressType.CHECK_SUM_256;
    }

    public boolean isCyberNameCollection() {
        return compressType == CompressType.CYBER_NAME_COLLECTION;
    }

    public boolean isNullableShort() {
        return compressType == CompressType.NULLABLE_SHORT;
    }

    public boolean isInt() {
        return compressType == CompressType.INT;
    }

    public boolean isVariableUInt() {
        return compressType == CompressType.VARIABLE_UINT;
    }

    public boolean isLong() {
        return compressType == CompressType.LONG;
    }

    public boolean isFloat() {
        return compressType == CompressType.FLOAT;
    }

    public boolean isBytes() {
        return compressType == CompressType.BYTES;
    }

    public boolean isStringCollection() {
        return compressType == CompressType.STRING_COLLECTION;
    }

    public boolean isString() {
        return compressType == CompressType.STRING;
    }

    public boolean isNullableString() {
        return compressType == CompressType.NULLABLE_STRING;
    }

    public boolean isSymbolCode() {
        return compressType == CompressType.SYMBOL_CODE;
    }

    public boolean isSymbol() {
        return compressType == CompressType.SYMBOL;
    }

    public boolean isCollection() {
        return compressType == CompressType.COLLECTION;
    }

    public boolean isAccountNameCollection() {
        return compressType == CompressType.ACCOUNT_NAME_COLLECTION;
    }

    public boolean isBool() {
        return compressType == CompressType.BOOL;
    }

    public boolean isChild() {
        return compressType == CompressType.CHILD;
    }

    public FieldModel(String name, String classType, CompressType compressType) {
        this.name = name;
        this.classType = classType;
        this.compressType = compressType;
    }
}
