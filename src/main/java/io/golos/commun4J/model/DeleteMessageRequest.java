package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.NameCompress;
import com.memtrip.eos.abi.writer.StringCompress;

@Abi
public class DeleteMessageRequest {
    private CommunName author;
    private String permlink;

    public DeleteMessageRequest(CommunName author, String permlink) {
        this.author = author;
        this.permlink = permlink;
    }

    @NameCompress
    public String getAuthor() {
        return author.getName();
    }

    @StringCompress
    public String getPermlink() {
        return permlink;
    }
}
