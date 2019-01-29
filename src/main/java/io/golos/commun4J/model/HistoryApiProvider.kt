package io.golos.commun4J.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor


interface HistoryApiProvider {
    fun getDisucssions(): List<Comun4jDiscussion>
    fun getDiscussion(author: CommunName, permlink: String): Comun4jDiscussion
}

class WebApi : HistoryApiProvider {
    private val httpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    private val postsUrl = "https://cyberway.golos.io/api/v1/posts/"
    private val moshi = Moshi.Builder().build()
    val listType = Types.newParameterizedType(List::class.java, Comun4jDiscussion::class.java)


    override fun getDisucssions(): List<Comun4jDiscussion> {
        val respString = httpClient.newCall(Request.Builder().url(postsUrl).get().build()).execute().body()!!.string()
        println("postsUrl =$postsUrl")
        println("response  =$respString")
        return moshi.adapter<List<Comun4jDiscussion>>(listType).fromJson(respString).orEmpty()
    }

    override fun getDiscussion(author: CommunName, permlink: String): Comun4jDiscussion {
        val respString = httpClient.newCall(Request.Builder()
                .url("$postsUrl/${author.name}/$permlink").get().build()).execute().body()!!.string()
        return moshi.adapter<Comun4jDiscussion>(Comun4jDiscussion::class.java).fromJson(respString)
                ?: Comun4jDiscussion(author.name, permlink, "", "")
    }
}