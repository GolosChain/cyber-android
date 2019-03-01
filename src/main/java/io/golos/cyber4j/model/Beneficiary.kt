package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.IntCompress
import com.memtrip.eos.abi.writer.NameCompress

@Abi
class Beneficiary(private val account: CyberName,
                  private val deductprcnt: Int) {


    val getAccount: String
        @NameCompress get() = account.name

    val getDeduct: Int
        @IntCompress get() = deductprcnt

}
