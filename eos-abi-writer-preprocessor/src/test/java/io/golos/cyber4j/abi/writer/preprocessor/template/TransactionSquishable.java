package io.golos.cyber4j.abi.writer.preprocessor.template;

import com.memtrip.eos.core.crypto.EosPublicKey;
import com.memtrip.eos.abi.writer.ByteWriter;
import com.memtrip.eos.abi.writer.Squishable;
import io.golos.cyber4j.abi.writer.preprocessor.model.Transaction;
import io.golos.abi.writer.ByteWriter;
import io.golos.abi.writer.Squishable;
import io.golos.cyber4j.abi.writer.preprocessor.model.Transaction;
import io.golos.cyber4j.abi.writer.ByteWriter;
import io.golos.cyber4j.abi.writer.Squishable;
import io.golos.cyber4j.abi.writer.preprocessor.model.Transaction;

public class TransactionSquishable implements Squishable<Transaction> {

    private final AbiBinaryGen abiBinaryGen;

    TransactionSquishable(AbiBinaryGen abiBinaryGen) {
        this.abiBinaryGen = abiBinaryGen;
    }

    @Override
    public void squish(Transaction transaction, ByteWriter byteWriter) {
        byteWriter.putInt((int)(transaction.getExpiration() / 1000));
        byteWriter.putBlockNum(transaction.getRefBlockNum());
        byteWriter.putBlockPrefix(transaction.getRefBlockPrefix());
        byteWriter.putVariableUInt(transaction.getMaxNetUsageWords());
        byteWriter.putVariableUInt(transaction.getMaxCpuUsageMs());
        byteWriter.putVariableUInt(transaction.getDelaySec());
        byteWriter.putFloat(transaction.getSpeed());
        byteWriter.putPublicKey(new EosPublicKey(transaction.getPublicKey()));
        byteWriter.putAsset(transaction.getAsset());
        byteWriter.putChainId(transaction.getChainId());
        byteWriter.putHexCollection(transaction.getHexCollection());

        abiBinaryGen.squishCollectionPackedAction(
            transaction.getContextFreeActions(),
            byteWriter);

        abiBinaryGen.squishCollectionPackedAction(
            transaction.getActions(),
            byteWriter);

        abiBinaryGen.squishTransaction(transaction);

        byteWriter.putStringCollection(transaction.getTransactionExtensions());
    }
}
