package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;

public class TagSquishable implements Squishable<Tag> {

  private final AbiBinaryGenRomo abiBinaryGen;

  TagSquishable(AbiBinaryGenRomo abiBinaryGen) {
    this.abiBinaryGen = abiBinaryGen;
  }

  @Override
  public void squish(Tag tag, ByteWriter byteWriter) {
    byteWriter.putString(tag.getTag());
  }
}
