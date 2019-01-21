package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class CreatePostRequestSquishable implements Squishable<CreatePostRequest> {

  private final AbiBinaryGenRomo abiBinaryGen;

  CreatePostRequestSquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(CreatePostRequest createpostrequest, ByteWriter byteWriter) {
    byteWriter.putName(createpostrequest.getAccount());
    byteWriter.putString(createpostrequest.getPermlink());
    byteWriter.putName(createpostrequest.getParentacc());
    byteWriter.putString(createpostrequest.getParentprmlnk());
    abiBinaryGen.squishCollectionBeneficiary(createpostrequest.getBeneficiaries(), byteWriter);
    byteWriter.putLong(createpostrequest.getTokenprop());
    byteWriter.putByte(createpostrequest.isVestpayment());
    byteWriter.putString(createpostrequest.getHeadermssg());
    byteWriter.putString(createpostrequest.getBodymssg());
    byteWriter.putString(createpostrequest.getLanguagemssg());
    abiBinaryGen.squishCollectionTag(createpostrequest.getTags(), byteWriter);
    byteWriter.putString(createpostrequest.getJsonmetadata());
  }
}
