package io.golos.cyber4j.model;

public class ResultDiscussionId {
    private CyberName author;
    private String permlink;
    private long ref_block_num;

    public ResultDiscussionId(CyberName author, String permlink, long ref_block_num) {
        this.author = author;
        this.permlink = permlink;
        this.ref_block_num = ref_block_num;
    }

    public CyberName getAuthor() {
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
