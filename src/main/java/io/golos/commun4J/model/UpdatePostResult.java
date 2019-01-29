package io.golos.commun4J.model;

import java.util.List;

public class UpdatePostResult {
    private CommunName account;
    private String permlink;
    private CommunName parentacc;
    private String parentprmlnk;
    private List<Beneficiary> beneficiaries;
    private long tokenprop = 0;
    private boolean vestpayment = true;
    private String headermssg;
    private String bodymssg;
    private String languagemssg = "ru";
    private List<Tag> tags;
    private String jsonmetadata = "";

    public UpdatePostResult(String account, String permlink, String parentacc,
                            String parentprmlnk, List<Beneficiary> beneficiaries,
                            long tokenprop, boolean vestpayment, String headermssg,
                            String bodymssg, List<Tag> tags, String language, String jsonMetadata) {
        this.account = new CommunName(account);
        this.permlink = permlink;
        this.parentacc = new CommunName(parentacc);
        this.parentprmlnk = parentprmlnk;
        this.beneficiaries = beneficiaries;
        this.tokenprop = tokenprop;
        this.vestpayment = vestpayment;
        this.headermssg = headermssg;
        this.bodymssg = bodymssg;
        this.tags = tags;
        this.languagemssg = language;
        this.jsonmetadata = jsonMetadata;
    }

    @Override
    public String toString() {
        return "CreatePostRequest{" +
                "account='" + account + '\'' +
                ", permlink='" + permlink + '\'' +
                ", parentacc='" + parentacc + '\'' +
                ", parentprmlnk='" + parentprmlnk + '\'' +
                ", beneficiaries=" + beneficiaries +
                ", tokenprop=" + tokenprop +
                ", vestpayment=" + vestpayment +
                ", headermssg='" + headermssg + '\'' +
                ", bodymssg='" + bodymssg + '\'' +
                ", languagemssg='" + languagemssg + '\'' +
                ", tags=" + tags +
                ", jsonmetadata=" + jsonmetadata +
                '}';
    }


    public CommunName getAccount() {
        return account;
    }


    public String getPermlink() {
        return permlink;
    }


    public CommunName getParentacc() {
        return parentacc;
    }


    public String getParentprmlnk() {
        return parentprmlnk;
    }


    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }


    public long getTokenprop() {
        return tokenprop;
    }


    public byte isVestpayment() {
        return (byte) (vestpayment ? 1 : 0);
    }


    public String getHeadermssg() {
        return headermssg;
    }


    public String getBodymssg() {
        return bodymssg;
    }


    public String getLanguagemssg() {
        return languagemssg;
    }


    public List<Tag> getTags() {
        return tags;
    }


    public String getJsonmetadata() {
        return jsonmetadata;
    }

    public UpdatePostResult() {
    }

    public void setAccount(String account) {
        this.account = new CommunName(account);
    }

    public void setPermlink(String permlink) {
        this.permlink = permlink;
    }

    public void setParentacc(String parentacc) {
        this.parentacc = new CommunName(parentacc);
    }

    public void setParentprmlnk(String parentprmlnk) {
        this.parentprmlnk = parentprmlnk;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public void setTokenprop(long tokenprop) {
        this.tokenprop = tokenprop;
    }

    public void setVestpayment(boolean vestpayment) {
        this.vestpayment = vestpayment;
    }

    public void setHeadermssg(String headermssg) {
        this.headermssg = headermssg;
    }

    public void setBodymssg(String bodymssg) {
        this.bodymssg = bodymssg;
    }

    public void setLanguagemssg(String languagemssg) {
        this.languagemssg = languagemssg;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setJsonmetadata(String jsonmetadata) {
        this.jsonmetadata = jsonmetadata;
    }
}
