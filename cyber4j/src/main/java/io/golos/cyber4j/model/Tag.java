package io.golos.cyber4j.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.StringCompress;

@Abi
public class Tag {
    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    @StringCompress
    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
