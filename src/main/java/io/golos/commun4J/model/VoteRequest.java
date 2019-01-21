package io.golos.commun4J.model;

// {
//    "name": "vote",
//    "base": "",
//    "fields": [
//      {
//        "name": "voter",
//        "type": "name"
//      },
//      {
//        "name": "author",
//        "type": "name"
//      },
//      {
//        "name": "permlink",
//        "type": "string"
//      },
//      {
//        "name": "weight",
//        "type": "uint16"
//      }
//    ]
//  },
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

    public CommunName getName() {
        return name;
    }

    public void setName(CommunName name) {
        this.name = name;
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

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
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
