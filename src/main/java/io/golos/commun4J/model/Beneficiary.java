package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.IntCompress;
import com.memtrip.eos.abi.writer.NameCompress;

@Abi
public class Beneficiary {
    //deductprcnt
    private String account;
    private int deductprcnt;

    public Beneficiary(CommunName account, int deductprcnt) {
        this.account = account.getName();
        this.deductprcnt = deductprcnt;
    }

    @NameCompress
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @IntCompress
    public int getDeductprcnt() {
        return deductprcnt;
    }

    public void setDeductprcnt(int deductprcnt) {
        this.deductprcnt = deductprcnt;
    }
}
