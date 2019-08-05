package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.Abi
import com.memtrip.eos.abi.writer.AssetCompress
import com.memtrip.eos.abi.writer.NameCompress
import com.memtrip.eos.abi.writer.StringCompress
import io.golos.sharedmodel.CyberName

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