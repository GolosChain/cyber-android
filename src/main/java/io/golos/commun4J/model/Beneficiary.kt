package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
class Beneficiary(private val account: CommunName,
                  private val deductprcnt: Int) {


    val getAccount: String
        @NameCompress get() = account.name

    val getDeduct: Int
        @IntCompress get() = deductprcnt

}
