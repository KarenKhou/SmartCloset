//package com.example.tesy2.data.repository
//
//
//import io.ktor.client.*
////import io.ktor.client.engine.okhttp.*
//import io.ktor.client.plugins.contentnegotiation.*
////import io.ktor.serialization.gson.*
//import okhttp3.OkHttpClient
//import java.security.SecureRandom
//import java.security.cert.X509Certificate
//import javax.net.ssl.*
//
//fun createUnsafeKtorClient(): HttpClient {
//    val trustAllCerts = arrayOf<TrustManager>(
//        object : X509TrustManager {
//            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
//            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
//            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
//        }
//    )
//
//    val sslContext = SSLContext.getInstance("SSL")
//    sslContext.init(null, trustAllCerts, SecureRandom())
//    val sslSocketFactory = sslContext.socketFactory
//
//    val unsafeOkHttpClient = OkHttpClient.Builder()
//        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
//        .hostnameVerifier { _, _ -> true }
//        .build()
//
//    return HttpClient(OkHttp) {
//        engine {
//            preconfigured = unsafeOkHttpClient
//        }
//        install(ContentNegotiation) {
//            gson()
//        }
//    }
//}
//
//
//
