package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.ByteCompress;
import com.memtrip.eos.abi.writer.CollectionCompress;
import com.memtrip.eos.abi.writer.LongCompress;
import com.memtrip.eos.abi.writer.NameCompress;
import com.memtrip.eos.abi.writer.StringCompress;

import java.util.List;

//account,
//        permlink,
//        parentacc: '',
//        parentprmlnk: '',
//        beneficiaries: [],
//        tokenprop: 0,
//        vestpayment: true,
//        headermssg: title,
//        bodymssg: html,
//        languagemssg: 'ru',
//        tags: [{ tag: 'com.test' }],
//        jsonmetadata: '',
@Abi
public class CreatePostRequest {

    private String account;
    private String permlink;
    private String parentacc;
    private String parentprmlnk;
    private List<Beneficiary> beneficiaries;
    private long tokenprop = 0;
    private boolean vestpayment = true;
    private String headermssg;
    private String bodymssg;
    private final String languagemssg = "ru";
    private List<Tag> tags;
    private final String jsonmetadata = "";

    public CreatePostRequest(String account, String permlink, String parentacc,
                             String parentprmlnk, List<Beneficiary> beneficiaries,
                             long tokenprop, boolean vestpayment, String headermssg,
                             String bodymssg, List<Tag> tags) {
        this.account = account;
        this.permlink = permlink;
        this.parentacc = parentacc;
        this.parentprmlnk = parentprmlnk;
        this.beneficiaries = beneficiaries;
        this.tokenprop = tokenprop;
        this.vestpayment = vestpayment;
        this.headermssg = headermssg;
        this.bodymssg = bodymssg;
        this.tags = tags;
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

    @NameCompress
    public String getAccount() {
        return account;
    }


    @StringCompress
    public String getPermlink() {
        return permlink;
    }


    @NameCompress
    public String getParentacc() {
        return parentacc;
    }


    @StringCompress
    public String getParentprmlnk() {
        return parentprmlnk;
    }


    @CollectionCompress
    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }


    @LongCompress
    public long getTokenprop() {
        return tokenprop;
    }


    @ByteCompress
    public byte isVestpayment() {
        return (byte) (vestpayment ? 1 : 0);
    }


    @StringCompress
    public String getHeadermssg() {
        return headermssg;
    }


    @StringCompress
    public String getBodymssg() {
        return bodymssg;
    }


    @StringCompress
    public String getLanguagemssg() {
        return languagemssg;
    }


    @CollectionCompress
    public List<Tag> getTags() {
        return tags;
    }


    @StringCompress
    public String getJsonmetadata() {
        return jsonmetadata;
    }


}
