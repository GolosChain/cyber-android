package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class MyActionAbiSquishable implements Squishable<MyActionAbi> {

  private final AbiBinaryGenRomo abiBinaryGen;

  MyActionAbiSquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(MyActionAbi myactionabi, ByteWriter byteWriter) {
    byteWriter.putName(myactionabi.getGetAccount());
    byteWriter.putName(myactionabi.getGetName());
    abiBinaryGen.squishCollectionMyTransactionAuthorizationAbi(
        myactionabi.getGetAuthorization(), byteWriter);
    byteWriter.putData(myactionabi.getGetData());
  }
}
