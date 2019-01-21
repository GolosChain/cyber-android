package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.bytewriter.DefaultByteWriter;
import com.memtrip.eos.abi.writer.compression.CompressionFactory;
import com.memtrip.eos.abi.writer.compression.CompressionType;
import com.memtrip.eos.core.hex.DefaultHexWriter;
import com.memtrip.eos.core.hex.HexWriter;

import java.util.List;

public class AbiBinaryGenRomo {

    private final ByteWriter byteWriter;
    private final HexWriter hexWriter;
    private final CompressionType compressionType;

    private final BeneficiarySquishable beneficiarySquishable;
    private final CreatePostRequestSquishable createpostrequestSquishable;
    private final TagSquishable tagSquishable;
    private final MyActionAbiSquishable myactionabiSquishable;
    private final MySignedTransactionAbiSquishable mysignedtransactionabiSquishable;
    private final MyTransactionAbiSquishable mytransactionabiSquishable;
    private final MyTransactionAuthorizationAbiSquishable mytransactionauthorizationabiSquishable;

    public AbiBinaryGenRomo(CompressionType compressionType) {
        this(new DefaultByteWriter(512), new DefaultHexWriter(), compressionType);
    }

    public AbiBinaryGenRomo(
            ByteWriter byteWriter, HexWriter hexWriter, CompressionType compressionType) {
        this.byteWriter = byteWriter;
        this.hexWriter = hexWriter;
        this.compressionType = compressionType;

        this.beneficiarySquishable = new BeneficiarySquishable(this);
        this.createpostrequestSquishable = new CreatePostRequestSquishable(this);
        this.tagSquishable = new TagSquishable(this);
        this.myactionabiSquishable = new MyActionAbiSquishable(this);
        this.mysignedtransactionabiSquishable = new MySignedTransactionAbiSquishable(this);
        this.mytransactionabiSquishable = new MyTransactionAbiSquishable(this);
        this.mytransactionauthorizationabiSquishable =
                new MyTransactionAuthorizationAbiSquishable(this);
    }

    public byte[] toBytes() {
        return new CompressionFactory(compressionType).create().compress(byteWriter.toBytes());
    }

    public String toHex() {
        byte[] compressedBytes = toBytes();
        return hexWriter.bytesToHex(compressedBytes, 0, compressedBytes.length, null);
    }

    public AbiBinaryGenRomo squishBeneficiary(Beneficiary beneficiary) {
        beneficiarySquishable.squish(beneficiary, byteWriter);
        return this;
    }

    void squishCollectionBeneficiary(List<Beneficiary> beneficiaryList, ByteWriter byteWriter) {
        byteWriter.putVariableUInt(beneficiaryList.size());
        for (Beneficiary beneficiary : beneficiaryList) {
            beneficiarySquishable.squish(beneficiary, byteWriter);
        }
    }

    public AbiBinaryGenRomo squishCreatePostRequest(CreatePostRequest createpostrequest) {
        createpostrequestSquishable.squish(createpostrequest, byteWriter);
        return this;
    }

    void squishCollectionCreatePostRequest(
            List<CreatePostRequest> createpostrequestList, ByteWriter byteWriter) {
        byteWriter.putVariableUInt(createpostrequestList.size());
        for (CreatePostRequest createpostrequest : createpostrequestList) {
            createpostrequestSquishable.squish(createpostrequest, byteWriter);
        }
    }

    public AbiBinaryGenRomo squishUpdatePost(UpdatePostRequest updatePostRequest) {
        byteWriter.putName(updatePostRequest.getPostAuthor().getName());
        byteWriter.putString(updatePostRequest.getPermlink());
        byteWriter.putString(updatePostRequest.getTitle());
        byteWriter.putString(updatePostRequest.getBody());
        byteWriter.putString(updatePostRequest.getLanguage());
        squishCollectionTag(updatePostRequest.getTags(), byteWriter);
        byteWriter.putString(updatePostRequest.getJsonmetadata());
        return this;
    }

    public AbiBinaryGenRomo squishTag(Tag tag) {
        tagSquishable.squish(tag, byteWriter);
        return this;
    }

    public AbiBinaryGenRomo squishVote(VoteRequest voteRequest) {
        byteWriter.putName(voteRequest.getName().getName());
        byteWriter.putName(voteRequest.getAuthor().getName());
        byteWriter.putString(voteRequest.getPermlink());
        byteWriter.putInt(voteRequest.getWeight());
        return this;
    }

    public AbiBinaryGenRomo squishUnVote(UnVoteRequest voteRequest) {
        byteWriter.putName(voteRequest.getVoter().getName());
        byteWriter.putName(voteRequest.getAuthor().getName());
        byteWriter.putString(voteRequest.getPermlink());
        return this;
    }


    void squishCollectionTag(List<Tag> tagList, ByteWriter byteWriter) {
        byteWriter.putVariableUInt(tagList.size());
        for (Tag tag : tagList) {
            tagSquishable.squish(tag, byteWriter);
        }
    }

    public AbiBinaryGenRomo squishMyActionAbi(MyActionAbi myactionabi) {
        myactionabiSquishable.squish(myactionabi, byteWriter);
        return this;
    }

    void squishCollectionMyActionAbi(List<MyActionAbi> myactionabiList, ByteWriter byteWriter) {
        byteWriter.putVariableUInt(myactionabiList.size());
        for (MyActionAbi myactionabi : myactionabiList) {
            myactionabiSquishable.squish(myactionabi, byteWriter);
        }
    }

    public AbiBinaryGenRomo squishMySignedTransactionAbi(
            MySignedTransactionAbi mysignedtransactionabi) {
        mysignedtransactionabiSquishable.squish(mysignedtransactionabi, byteWriter);
        return this;
    }

    void squishCollectionMySignedTransactionAbi(
            List<MySignedTransactionAbi> mysignedtransactionabiList, ByteWriter byteWriter) {
        byteWriter.putVariableUInt(mysignedtransactionabiList.size());
        for (MySignedTransactionAbi mysignedtransactionabi : mysignedtransactionabiList) {
            mysignedtransactionabiSquishable.squish(mysignedtransactionabi, byteWriter);
        }
    }

    public AbiBinaryGenRomo squishMyTransactionAbi(MyTransactionAbi mytransactionabi) {
        mytransactionabiSquishable.squish(mytransactionabi, byteWriter);
        return this;
    }

    void squishCollectionMyTransactionAbi(
            List<MyTransactionAbi> mytransactionabiList, ByteWriter byteWriter) {
        byteWriter.putVariableUInt(mytransactionabiList.size());
        for (MyTransactionAbi mytransactionabi : mytransactionabiList) {
            mytransactionabiSquishable.squish(mytransactionabi, byteWriter);
        }
    }

    public AbiBinaryGenRomo squishMyTransactionAuthorizationAbi(
            MyTransactionAuthorizationAbi mytransactionauthorizationabi) {
        mytransactionauthorizationabiSquishable.squish(mytransactionauthorizationabi, byteWriter);
        return this;
    }

    void squishCollectionMyTransactionAuthorizationAbi(
            List<MyTransactionAuthorizationAbi> mytransactionauthorizationabiList,
            ByteWriter byteWriter) {
        byteWriter.putVariableUInt(mytransactionauthorizationabiList.size());
        for (MyTransactionAuthorizationAbi mytransactionauthorizationabi :
                mytransactionauthorizationabiList) {
            mytransactionauthorizationabiSquishable.squish(mytransactionauthorizationabi, byteWriter);
        }
    }
}
