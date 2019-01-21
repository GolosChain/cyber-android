package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class MySignedTransactionAbiSquishable implements Squishable<MySignedTransactionAbi> {

  private final AbiBinaryGenRomo abiBinaryGen;

  MySignedTransactionAbiSquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(MySignedTransactionAbi mysignedtransactionabi, ByteWriter byteWriter) {
    byteWriter.putChainId(mysignedtransactionabi.getGetChainId());
    abiBinaryGen.squishMyTransactionAbi(mysignedtransactionabi.getGetTransaction());
    byteWriter.putHexCollection(mysignedtransactionabi.getGetContextFreeData());
  }
}
