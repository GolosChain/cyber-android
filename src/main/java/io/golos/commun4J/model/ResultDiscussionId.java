package io.golos.commun4J.model;

public class ResultDiscussionId {
    private CommunName author;
    private String permlink;
    private long ref_block_num;

    public ResultDiscussionId(CommunName author, String permlink, long ref_block_num) {
        this.author = author;
        this.permlink = permlink;
        this.ref_block_num = ref_block_num;
    }

    public CommunName getAuthor() {
        return author;
    }

    public String getPermlink() {
        return permlink;
    }

    public long getRef_block_num() {
        return ref_block_num;
    }


    @Override
    public String toString() {
        return "ResultDiscussionId{" +
                "author='" + author + '\'' +
                ", permlink='" + permlink + '\'' +
                ", ref_block_num=" + ref_block_num +
                '}';
    }
}
