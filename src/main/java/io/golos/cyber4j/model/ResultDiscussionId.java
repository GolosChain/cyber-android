package io.golos.cyber4j.model;

public class ResultDiscussionId {
    private CyberName author;
    private String permlink;

    public ResultDiscussionId(CyberName author, String permlink) {
        this.author = author;
        this.permlink = permlink;
    }

    public CyberName getAuthor() {
        return author;
    }

    public String getPermlink() {
        return permlink;
    }



    @Override
    public String toString() {
        return "ResultDiscussionId{" +
                "author='" + author + '\'' +
                ", permlink='" + permlink + '\'' +
                ", ref_block_num=" +
                '}';
    }
}
