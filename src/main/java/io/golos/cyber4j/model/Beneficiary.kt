package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.*

@Abi
class Beneficiary(private val account: CyberName,
                  private val deductprcnt: Short) {


    val getAccount: String
        @NameCompress get() = account.name

    val getDeduct: Short
        @ShortCompress get() = deductprcnt

}
