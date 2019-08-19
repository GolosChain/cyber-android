package io.golos.cyber4j.model

import io.golos.cyber4j.abi.writer.Abi
import io.golos.cyber4j.abi.writer.AssetCompress
import io.golos.cyber4j.abi.writer.NameCompress
import io.golos.cyber4j.abi.writer.StringCompress
import io.golos.cyber4j.sharedmodel.CyberName

@Abi
class IssueRequestAbi(private val to: CyberName,
                      private val quantity: String,
                      private val memo: String) {

    val getForUser: String
        @NameCompress get() = to.name

    val getAmount: String
        @AssetCompress get() = quantity
    val getMemo: String
        @StringCompress get() = memo
}