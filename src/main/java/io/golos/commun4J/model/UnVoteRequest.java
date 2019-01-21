package io.golos.commun4J.model;

// {
//    "voter": "vote",
//    "base": "",
//    "fields": [
//      {
//        "voter": "voter",
//        "type": "voter"
//      },
//      {
//        "voter": "author",
//        "type": "voter"
//      },
//      {
//        "voter": "permlink",
//        "type": "string"
//      },
//      {
//        "voter": "weight",
//        "type": "uint16"
//      }
//    ]
//  },
public class UnVoteRequest {
    private CommunName voter;
    private CommunName author;
    private String permlink;


    public UnVoteRequest(CommunName voter, CommunName author, String permlink) {
        this.voter = voter;
        this.author = author;
        this.permlink = permlink;

    }

    public CommunName getVoter() {
        return voter;
    }

    public void setVoter(CommunName voter) {
        this.voter = voter;
    }

    public CommunName getAuthor() {
        return author;
    }

    public void setAuthor(CommunName author) {
        this.author = author;
    }

    public String getPermlink() {
        return permlink;
    }

    public void setPermlink(String permlink) {
        this.permlink = permlink;
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
