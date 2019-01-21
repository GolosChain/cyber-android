package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class MyTransactionAbiSquishable implements Squishable<MyTransactionAbi> {

  private final AbiBinaryGenRomo abiBinaryGen;

  MyTransactionAbiSquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(MyTransactionAbi mytransactionabi, ByteWriter byteWriter) {
    byteWriter.putTimestampMs(mytransactionabi.getGetExpiration());
    byteWriter.putBlockNum(mytransactionabi.getGetRefBlockNum());
    byteWriter.putBlockPrefix(mytransactionabi.getGetRefBlockPrefix());
    byteWriter.putVariableUInt(mytransactionabi.getGetMaxNetUsageWords());
    byteWriter.putVariableUInt(mytransactionabi.getGetMaxCpuUsageMs());
    byteWriter.putVariableUInt(mytransactionabi.getGetDelaySec());
    abiBinaryGen.squishCollectionMyActionAbi(
        mytransactionabi.getGetContextFreeActions(), byteWriter);
    abiBinaryGen.squishCollectionMyActionAbi(mytransactionabi.getGetActions(), byteWriter);
    byteWriter.putStringCollection(mytransactionabi.getGetTransactionExtensions());
  }
}
