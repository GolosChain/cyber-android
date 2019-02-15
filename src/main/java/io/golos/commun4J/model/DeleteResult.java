package io.golos.commun4J.model;

public class DeleteResult {
    private ResultDiscussionId message_id;

    public DeleteResult(ResultDiscussionId message_id) {
        this.message_id = message_id;
    }

    public ResultDiscussionId getMessage_id() {
        return message_id;
    }

    public void setMessage_id(ResultDiscussionId message_id) {
        this.message_id = message_id;
    }

    @Override
    public String toString() {
        return "DeleteResult{" +
                "message_id=" + message_id +
                '}';
    }
}
