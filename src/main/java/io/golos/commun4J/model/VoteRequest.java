package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.IntCompress;
import com.memtrip.eos.abi.writer.NameCompress;
import com.memtrip.eos.abi.writer.StringCompress;

@Abi
public class VoteRequest {
    private CommunName name;
    private CommunName author;
    private String permlink;
    private short weight;

    public VoteRequest(CommunName name, CommunName author, String permlink, short weight) {
        this.name = name;
        this.author = author;
        this.permlink = permlink;
        this.weight = weight;
    }

    @NameCompress
    public String getName() {
        return name.getName();
    }

    @NameCompress
    public String getAuthor() {
        return author.getName();
    }

    @StringCompress
    public String getPermlink() {
        return permlink;
    }

    @IntCompress
    public short getWeight() {
        return weight;
    }


    @Override
    public String toString() {
        return "VoteRequest{" +
                "name=" + name +
                ", author=" + author +
                ", permlink='" + permlink + '\'' +
                ", weight=" + weight +
                '}';
    }
}
