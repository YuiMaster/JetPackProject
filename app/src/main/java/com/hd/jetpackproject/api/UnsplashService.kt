package com.hd.jetpackproject.api

import com.hd.jetpackproject.data.BannerResponse
import com.hd.jetpackproject.data.UnsplashSearchResponse
import com.hd.jetpackproject.di.PostInterceptor
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.logging.Logger


interface UnsplashService {

    @GET("list/405/1/json")
    suspend fun searchList(
        @Query("k") key: String
    ): UnsplashSearchResponse

    @GET("banner/json")
    suspend fun requestBanner(): BannerResponse

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"
//        private const val BASE_URL = "https://wanandroid.com/wxarticle/"

        fun create(): UnsplashService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder().addInterceptor(logger).addInterceptor { chain: Interceptor.Chain ->
                val builder1 = chain.request().newBuilder()
                val request = builder1.build()
                chain.proceed(request)
            }.addInterceptor(PostInterceptor()).build()

            return Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()
                .create(UnsplashService::class.java)
        }
    }
}