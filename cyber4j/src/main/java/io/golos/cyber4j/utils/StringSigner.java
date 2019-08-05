package io.golos.cyber4j.utils;

import com.memtrip.eos.core.crypto.EosPrivateKey;
import com.memtrip.eos.core.crypto.signature.PrivateKeySigning;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class StringSigner {
    public static String signString(@NotNull String stringToSign,
                                    @NotNull String privateActiveKey) {
        return new PrivateKeySigning().sign(
                stringToSign.getBytes(StandardCharsets.UTF_8),
                new EosPrivateKey(privateActiveKey)
        );
    }

}
