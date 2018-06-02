package com.yulocus.eventplayer.api

import com.yulocus.eventplayer.BuildConfig
import com.yulocus.eventplayer.MainApplication
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiServiceFactory {

    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit? {
        if(retrofit == null) {
            synchronized(ApiServiceFactory::class.java) {
                if (retrofit == null) {
                    // setup cache
                    val cacheFile = File(MainApplication.instance.cacheDir, "cache")
                    val cache = Cache(cacheFile, 1024 * 1024 * 50) // 50mb

                    val okHttpClient = OkHttpClient().newBuilder()
                            .readTimeout(ApiConstants.API_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .writeTimeout(ApiConstants.API_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .connectTimeout(ApiConstants.API_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .addInterceptor(addHttpLoggingInterceptor())
                            .retryOnConnectionFailure(true)
                            .cache(cache)
                            .build()

                    retrofit = Retrofit.Builder()
                            .baseUrl(ApiConstants.API_ENDPOINT)
                            .client(okHttpClient!!)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
            }
        }
        return retrofit
    }

    /**
     * Logging Interceptor
     */
    private fun addHttpLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }
}
