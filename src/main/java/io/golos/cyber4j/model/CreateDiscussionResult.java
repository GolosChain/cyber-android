package io.golos.cyber4j.model;

import java.util.List;

public class CreateDiscussionResult {
   private ResultDiscussionId message_id;
   private ResultDiscussionId parent_id;

    private List<Beneficiary> beneficiaries;
    private long tokenprop = 0;
    private boolean vestpayment = true;
    private String headermssg;
    private String bodymssg;
    private String languagemssg = "ru";
    private List<Tag> tags;
    private String jsonmetadata = "";

    public CreateDiscussionResult(ResultDiscussionId message_id, ResultDiscussionId parent_id, List<Beneficiary> beneficiaries, long tokenprop, boolean vestpayment, String headermssg, String bodymssg, String languagemssg, List<Tag> tags, String jsonmetadata) {
        this.message_id = message_id;
        this.parent_id = parent_id;
        this.beneficiaries = beneficiaries;
        this.tokenprop = tokenprop;
        this.vestpayment = vestpayment;
        this.headermssg = headermssg;
        this.bodymssg = bodymssg;
        this.languagemssg = languagemssg;
        this.tags = tags;
        this.jsonmetadata = jsonmetadata;
    }

    public ResultDiscussionId getMessage_id() {
        return message_id;
    }

    public void setMessage_id(ResultDiscussionId message_id) {
        this.message_id = message_id;
    }

    public ResultDiscussionId getParent_id() {
        return parent_id;
    }

    public void setParent_id(ResultDiscussionId parent_id) {
        this.parent_id = parent_id;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public long getTokenprop() {
        return tokenprop;
    }

    public void setTokenprop(long tokenprop) {
        this.tokenprop = tokenprop;
    }

    public boolean isVestpayment() {
        return vestpayment;
    }

    public void setVestpayment(boolean vestpayment) {
        this.vestpayment = vestpayment;
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

    public String getLanguagemssg() {
        return languagemssg;
    }

    public void setLanguagemssg(String languagemssg) {
        this.languagemssg = languagemssg;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getJsonmetadata() {
        return jsonmetadata;
    }

    public void setJsonmetadata(String jsonmetadata) {
        this.jsonmetadata = jsonmetadata;
    }

    @Override
    public String toString() {
        return "CreateDiscussionResult{" +
                "message_id=" + message_id +
                ", parent_id=" + parent_id +
                ", beneficiaries=" + beneficiaries +
                ", tokenprop=" + tokenprop +
                ", vestpayment=" + vestpayment +
                ", headermssg='" + headermssg + '\'' +
                ", bodymssg='" + bodymssg + '\'' +
                ", languagemssg='" + languagemssg + '\'' +
                ", tags=" + tags +
                ", jsonmetadata='" + jsonmetadata + '\'' +
                '}';
    }
}
