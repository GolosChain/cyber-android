package io.golos.cyber4j.model

import io.golos.cyber4j.abi.writer.Abi
import io.golos.cyber4j.abi.writer.NameCompress
import io.golos.cyber4j.abi.writer.ShortCompress
import io.golos.cyber4j.sharedmodel.CyberName

@Abi
class Beneficiary(private val account: CyberName,
                  private val deductprcnt: Short) {


    val getAccount: String
        @NameCompress get() = account.name

    val getDeduct: Short
        @ShortCompress get() = deductprcnt

}
