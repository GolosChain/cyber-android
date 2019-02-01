package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.BytesCompress;
import com.memtrip.eos.abi.writer.NameCompress;

@Abi
public class VestingStartRequest {
    private CommunName owner;

    public VestingStartRequest(CommunName owner) {
        this.owner = owner;
    }

    @NameCompress
    public String getOwner() {
        return owner.getName();
    }

    @BytesCompress
    public byte[] getDecsBytes() {
        return new byte[]{3, 71, 76, 83, 0, 0, 0, 0};
    }

    @NameCompress
    public String getRamPayer() {
        return "eosio";
    }
}