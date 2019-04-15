package io.golos.cyber4j.model

import com.memtrip.eos.http.rpc.model.account.request.AccountName
import com.memtrip.eos.http.rpc.model.account.response.Account
import com.memtrip.eos.http.rpc.model.block.request.BlockNumOrId
import com.memtrip.eos.http.rpc.model.block.response.Block
import com.memtrip.eos.http.rpc.model.block.response.BlockHeaderState
import com.memtrip.eos.http.rpc.model.contract.request.*
import com.memtrip.eos.http.rpc.model.contract.response.*
import com.memtrip.eos.http.rpc.model.info.Info
import com.memtrip.eos.http.rpc.model.producer.request.GetProducers
import com.memtrip.eos.http.rpc.model.producer.response.ProducerList
import com.memtrip.eos.http.rpc.model.signing.GetRequiredKeysBody
import com.memtrip.eos.http.rpc.model.signing.PushTransaction
import com.memtrip.eos.http.rpc.model.signing.RequiredKeys
import com.memtrip.eos.http.rpc.model.transaction.response.TransactionCommitted
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface CyberWayChainApi {

    fun getInfo(): Single<Response<Info>>

    fun getProducers(body: GetProducers): Single<Response<ProducerList>>

    fun getBlock(body: BlockNumOrId): Single<Response<Block>>

    fun getBlockHeaderState(body: BlockNumOrId): Single<Response<BlockHeaderState>>

    fun getAccount(body: AccountName): Single<Response<Account>>

    fun getAbi(body: AccountName): Single<Response<AbiForAccount>>

    fun getCode(body: GetCodeByAccountName): Single<Response<CodeForAccount>>

    fun getRawCodeAndAbi(body: AccountName): Single<Response<RawCodeForAccount>>

    fun getTableRows(body: GetTableRows): Single<Response<ContractTableRows>>

    fun getCurrencyBalance(body: GetCurrencyBalance): Single<Response<List<String>>>

    fun abiJsonToBin(body: RequestBody): Single<Response<BinaryHex>>

    fun abiBinToJson(body: AbiBinToJson): Single<Response<ResponseBody>>

    fun getRequiredKeys(body: GetRequiredKeysBody): Single<Response<RequiredKeys>>

    fun getCurrencyStats(body: GetCurrencyStats): Single<Response<ResponseBody>>

    fun pushTransaction(body: PushTransaction):  Single<Response<String>>

    fun resolveNames(body: List<String>): Single<List<ResolvedName>>
}