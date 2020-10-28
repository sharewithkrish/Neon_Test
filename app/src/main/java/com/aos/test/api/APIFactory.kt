package com.aos.test.api

import com.aos.test.MyApplication
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


object APIFactory {
    private const val enableCache: Boolean = true
    private const val cacheSize = 10 * 1024 * 1024 // 10 MB
    private val httpCacheDirectory = File(MyApplication.getInstance()?.cacheDir, "http-cache")
    private val cache = Cache(httpCacheDirectory, cacheSize.toLong())
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val networkCacheInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES)
                .build()
        response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
    }

    private val connectivityInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code() == 401) {
            throw LogOutException("unauthorized exception")
        }
        response
    }

    val httpClient: OkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder().apply {
        cache(if (enableCache) cache else null)
        readTimeout(5, TimeUnit.MINUTES)
        connectTimeout(5, TimeUnit.MINUTES)
        addInterceptor(interceptor)
        addInterceptor(connectivityInterceptor)
        addInterceptor(networkCacheInterceptor)

    }.build()

    val builder = Retrofit.Builder().apply {
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    }

    inline fun <reified T : Any> createService(): T {
        return builder.client(httpClient).baseUrl(APIConstants.BASE_URL).build().create(T::class.java)
    }

    class LogOutException(message: String) : IOException(message)
    class ApiFailureException(message: String) : IOException(message)
}
