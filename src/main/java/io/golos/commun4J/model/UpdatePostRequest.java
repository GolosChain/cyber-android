package io.golos.commun4J.model;

//{
//        "name": "updatemssg",
//        "base": "",
//        "fields": [
//        {
//        "name": "account",
//        "type": "name"
//        },
//        {
//        "name": "permlink",
//        "type": "string"
//        },
//        {
//        "name": "headermssg",
//        "type": "string"
//        },
//        {
//        "name": "bodymssg",
//        "type": "string"
//        },
//        {
//        "name": "languagemssg",
//        "type": "string"
//        },
//        {
//        "name": "tags",
//        "type": "tags[]"
//        },
//        {
//        "name": "jsonmetadata",
//        "type": "string"
//        }
//        ]
//        }

import java.util.List;

public class UpdatePostRequest {
    private CommunName postAuthor;
    private String permlink;
    private String title;
    private String body;
    private String language;
    private List<Tag> tags;
    private String jsonmetadata;

    public UpdatePostRequest(CommunName postAuthor, String permlink, String title, String body, String language, List<Tag> tags, String jsonmetadata) {
        this.postAuthor = postAuthor;
        this.permlink = permlink;
        this.title = title;
        this.body = body;
        this.language = language;
        this.tags = tags;
        this.jsonmetadata = jsonmetadata;
    }

    public CommunName getPostAuthor() {
        return postAuthor;
    }

    public String getPermlink() {
        return permlink;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getLanguage() {
        return language;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getJsonmetadata() {
        return jsonmetadata;
    }

    @Override
    public String toString() {
        return "UpdatePostRequest{" +
                "postAuthor=" + postAuthor +
                ", permlink='" + permlink + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", language='" + language + '\'' +
                ", tags=" + tags +
                ", jsonmetadata='" + jsonmetadata + '\'' +
                '}';
    }
}
