package io.golos.cyber4j.model;

import java.util.Objects;

public class ReblogResult {
    private CyberName rebloger;
    private ResultDiscussionId message_id;

    public ReblogResult(CyberName rebloger, ResultDiscussionId message_id) {
        this.rebloger = rebloger;
        this.message_id = message_id;
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

    @Override
    public String toString() {
        return "ReblogResult{" +
                "rebloger=" + rebloger +
                ", message_id=" + message_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReblogResult that = (ReblogResult) o;
        return Objects.equals(rebloger, that.rebloger) &&
                Objects.equals(message_id, that.message_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rebloger, message_id);
    }
}
