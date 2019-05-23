package io.golos.cyber4j.model;

import java.util.Objects;

public class ReblogResult {
    private CyberName rebloger;
    private ResultDiscussionId message_id;
    private String headermssg;
    private String bodymssg;


    public ReblogResult(CyberName rebloger, ResultDiscussionId message_id, String headermssg, String bodymssg) {
        this.rebloger = rebloger;
        this.message_id = message_id;
        this.headermssg = headermssg;
        this.bodymssg = bodymssg;
    }

    public CyberName getRebloger() {
        return rebloger;
    }

    public void setRebloger(CyberName rebloger) {
        this.rebloger = rebloger;
    }

    public ResultDiscussionId getMessage_id() {
        return message_id;
    }

    public void setMessage_id(ResultDiscussionId message_id) {

        this.message_id = message_id;
    }

    public String getHeadermssg() {
        return headermssg;
    }

    public void setHeadermssg(String headermssg) {
        this.headermssg = headermssg;
    }

    public String getBodymssg() {
        return bodymssg;
    }

    public void setBodymssg(String bodymssg) {
        this.bodymssg = bodymssg;
    }

    @Override
    public String toString() {
        return "ReblogResult{" +
                "rebloger=" + rebloger +
                ", message_id=" + message_id +
                ", headermssg='" + headermssg + '\'' +
                ", bodymssg='" + bodymssg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReblogResult that = (ReblogResult) o;
        return Objects.equals(rebloger, that.rebloger) &&
                Objects.equals(message_id, that.message_id) &&
                Objects.equals(headermssg, that.headermssg) &&
                Objects.equals(bodymssg, that.bodymssg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rebloger, message_id, headermssg, bodymssg);
    }
}
