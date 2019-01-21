package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class MyTransactionAuthorizationAbiSquishable
    implements Squishable<MyTransactionAuthorizationAbi> {

  private final AbiBinaryGenRomo abiBinaryGen;

  MyTransactionAuthorizationAbiSquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(
      MyTransactionAuthorizationAbi mytransactionauthorizationabi, ByteWriter byteWriter) {
    byteWriter.putName(mytransactionauthorizationabi.getGetActor());
    byteWriter.putName(mytransactionauthorizationabi.getGetPermission());
  }
}
