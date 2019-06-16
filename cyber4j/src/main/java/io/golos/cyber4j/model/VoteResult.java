package io.golos.cyber4j.model;

import io.golos.sharedmodel.CyberName;

public class VoteResult {

    private CyberName voter;
    private ResultDiscussionId message_id;
    private short weight;

    public VoteResult(CyberName voter, ResultDiscussionId message_id, short weight) {
        this.voter = voter;
        this.message_id = message_id;
        this.weight = weight;
    }


    public CyberName getVoter() {
        return voter;
    }

    public void setVoter(CyberName voter) {
        this.voter = voter;
    }

    public ResultDiscussionId getMessage_id() {
        return message_id;
    }

    public void setMessage_id(ResultDiscussionId message_id) {
        this.message_id = message_id;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "VoteResult{" +
                "voter=" + voter +
                ", message_id=" + message_id +
                ", weight=" + weight +
                '}';
    }
}
