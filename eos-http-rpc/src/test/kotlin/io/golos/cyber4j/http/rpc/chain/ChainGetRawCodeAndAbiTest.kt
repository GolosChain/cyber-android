package io.golos.cyber4j.http.rpc.chain

import io.golos.cyber4j.http.rpc.Api
import com.memtrip.eos.http.rpc.model.account.request.AccountName
import com.memtrip.eos.http.rpc.utils.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(JUnitPlatform::class)
class ChainGetRawCodeAndAbiTest : Spek({

    given("an Api") {

        val okHttpClient by memoized {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
        }

        val chainApi by memoized { io.golos.cyber4j.http.rpc.Api(Config.CHAIN_API_BASE_URL, okHttpClient).chain }

        on("v1/chain/get_raw_code_and_abi") {

            val rawCodeAndAbi = chainApi.getRawCodeAndAbi(AccountName("eosio.token")).blockingGet()

            it("should return the code deployed by the account") {
                assertTrue(rawCodeAndAbi.isSuccessful)
                assertNotNull(rawCodeAndAbi.body())
                assertNotNull(rawCodeAndAbi.body()!!.wasm)
                assertNotNull(rawCodeAndAbi.body()!!.abi)
                assertEquals(rawCodeAndAbi.body()!!.account_name, "eosio.token")
            }
        }
    }
})