package com.wl2776.autoclose

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

// self-signed ssl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Query
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

data class SLAPIDesc(val code: String?, val message: String?)

data class SLAPIResponse(val state: Int, val desc: SLAPIDesc)

/*
1. appID, secret -> get app code ('https://id.starline.ru/apiV3/application/getCode/') -> app code (1 hour)
2. appID, secret, appcode -> get app token ('https://id.starline.ru/apiV3/application/getToken/') -> apptoken (4 hrs)
3. apptoken, login, pwd -> get slid token ('https://id.starline.ru/apiV3/user/login/') -> slid token (1 year)
* */
interface StarlineIdAPI {
    @GET("apiV3/application/getCode")
    fun getAppCode(@Query("appId") appId: String,
                   @Query("secret") appSecret: String): Call<SLAPIResponse>

    @GET("apiV3/application/getToken/")
    fun getAppToken(): Call<String>

    @POST("apiV3/user/login/")
    fun getSLIdToken(): Call<String>
}

/*
4. slid_token -> get_slnet_token ('https://developer.starline.ru/json/v2/auth.slid') -> slnet token
*/
interface StarlineNetAPI {
    @POST("json/v2/auth.slid")
    fun getSLNetToken(): Call<String>
}

class MyManager : X509TrustManager {
    override fun checkServerTrusted(
        p0: Array<out X509Certificate>?,
        p1: String?
    ) {
        //allow all
    }

    override fun checkClientTrusted(
        p0: Array<out X509Certificate>?,
        p1: String?
    ) {
        //allow all
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}

fun createOkHttpClient(): OkHttpClient {
    return try {
        val interceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                HttpLoggingInterceptor.Level.BODY.also { httpLoggingInterceptor.level = it }
            }
        }

        val trustAllCerts: Array<TrustManager> = arrayOf(MyManager())
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        OkHttpClient.Builder()
            .addNetworkInterceptor (interceptor)
            .sslSocketFactory(sslSocketFactory, MyManager())
            .hostnameVerifier { _1: String?, _2: SSLSession? -> true }
            .build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}


class Login {
    private val service_id: StarlineIdAPI

    init {
        val client = createOkHttpClient()
        val retrofit = Retrofit.Builder()
                        .baseUrl(
                            "https://id.starline.ru/"
//                        "https://192.168.88.228:4443"
                        )
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
        service_id = retrofit.create(StarlineIdAPI::class.java)
    }

    fun login(callback: Callback<SLAPIResponse>) {
        val app_code_call = service_id.getAppCode("4595",
            "// MD5 sum of app secret")
        app_code_call.enqueue(callback)
    }
}