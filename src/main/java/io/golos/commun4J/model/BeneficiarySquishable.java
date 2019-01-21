package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class BeneficiarySquishable implements Squishable<Beneficiary> {

  private final AbiBinaryGenRomo abiBinaryGen;

  BeneficiarySquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(Beneficiary beneficiary, ByteWriter byteWriter) {
    byteWriter.putName(beneficiary.getAccount());
    byteWriter.putInt(beneficiary.getDeductprcnt());
  }
}
