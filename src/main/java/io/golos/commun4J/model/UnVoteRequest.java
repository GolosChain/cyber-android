package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.NameCompress;
import com.memtrip.eos.abi.writer.StringCompress;

@Abi
public class UnVoteRequest {
    private CommunName voter;
    private CommunName author;
    private String permlink;


    public UnVoteRequest(CommunName voter, CommunName author, String permlink) {
        this.voter = voter;
        this.author = author;
        this.permlink = permlink;

    }

    @NameCompress
    public String getVoter() {
        return voter.getName();
    }


    @NameCompress
    public String getAuthor() {
        return author.getName();
    }

    @StringCompress
    public String getPermlink() {
        return permlink;
    }


    @Override
    public String toString() {
        return "VoteRequest{" +
                "voter=" + voter +
                ", author=" + author +
                ", permlink='" + permlink + '\'' +
                '}';
    }
}
