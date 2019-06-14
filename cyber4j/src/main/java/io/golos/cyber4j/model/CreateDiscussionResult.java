package io.golos.cyber4j.model;

import java.util.List;
import java.util.Objects;

public class CreateDiscussionResult {
    private ResultDiscussionId message_id;
    private ResultDiscussionId parent_id;

    private List<BeneficiaryResult> beneficiaries;
    private long tokenprop = 0;
    private boolean vestpayment = true;
    private String headermssg;
    private String bodymssg;
    private String languagemssg = "ru";
    private List<String> tags;
    private String jsonmetadata = "";
    private Long curators_prcnt;

    public CreateDiscussionResult(ResultDiscussionId message_id, ResultDiscussionId parent_id, List<BeneficiaryResult> beneficiaries, long tokenprop, boolean vestpayment, String headermssg, String bodymssg, String languagemssg, List<String> tags, String jsonmetadata, Long curators_prcnt) {
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
        this.curators_prcnt = curators_prcnt;
    }

    public Long getCurators_prcnt() {
        return curators_prcnt;
    }

    public void setCurators_prcnt(Long curators_prcnt) {
        this.curators_prcnt = curators_prcnt;
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

    public List<BeneficiaryResult> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<BeneficiaryResult> beneficiaries) {
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getJsonmetadata() {
        return jsonmetadata;
    }

    public void setJsonmetadata(String jsonmetadata) {
        this.jsonmetadata = jsonmetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateDiscussionResult that = (CreateDiscussionResult) o;
        return tokenprop == that.tokenprop &&
                vestpayment == that.vestpayment &&
                Objects.equals(message_id, that.message_id) &&
                Objects.equals(parent_id, that.parent_id) &&
                Objects.equals(beneficiaries, that.beneficiaries) &&
                Objects.equals(headermssg, that.headermssg) &&
                Objects.equals(bodymssg, that.bodymssg) &&
                Objects.equals(languagemssg, that.languagemssg) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(jsonmetadata, that.jsonmetadata) &&
                Objects.equals(curators_prcnt, that.curators_prcnt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_id, parent_id, beneficiaries, tokenprop, vestpayment, headermssg, bodymssg, languagemssg, tags, jsonmetadata, curators_prcnt);
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
                ", curators_prcnt=" + curators_prcnt +
                '}';
    }

    static class BeneficiaryResult {
        private CyberName account;
        private short weight;

        public BeneficiaryResult(CyberName account, short weight) {
            this.account = account;
            this.weight = weight;
        }

        public CyberName getAccount() {
            return account;
        }

        public void setAccount(CyberName account) {
            this.account = account;
        }

        public short getWeight() {
            return weight;
        }

        public void setWeight(short weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "BeneficiaryResult{" +
                    "account=" + account +
                    ", weight=" + weight +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BeneficiaryResult that = (BeneficiaryResult) o;
            return weight == that.weight &&
                    Objects.equals(account, that.account);
        }

        @Override
        public int hashCode() {
            return Objects.hash(account, weight);
        }
    }
}
