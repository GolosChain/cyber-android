package io.golos.commun4J.model;

public class ProfileMetadataDeleteResult {
    private String account;

    public ProfileMetadataDeleteResult(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "ProfileMetadataDeleteResult{" +
                "account='" + account + '\'' +
                '}';
    }
}
