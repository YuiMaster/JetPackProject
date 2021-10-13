package com.hd.jetpackproject.api

import com.hd.jetpackproject.data.BannerResponse
import com.hd.jetpackproject.data.UnsplashSearchResponse
import com.hd.jetpackproject.utils.LOG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val service: UnsplashService) {

    suspend fun search(query: String): UnsplashSearchResponse {
        return service.searchList(query)
    }

    suspend fun reqBanner(): BannerResponse {
        LOG.d("UnsplashRepository", "requestBanner $service")
        return service.requestBanner()
    }
}