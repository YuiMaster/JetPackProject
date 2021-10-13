package com.hd.jetpackproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hd.jetpackproject.api.UnsplashRepository
import com.hd.jetpackproject.data.BannerResponse
import com.hd.jetpackproject.data.UnsplashSearchResponse
import com.hd.jetpackproject.utils.LOG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnsplashViewModel @Inject internal constructor(private val repository: UnsplashRepository) : ViewModel() {
    val liveQuery: MutableLiveData<UnsplashSearchResponse> = MutableLiveData()
    val liveBanner: MutableLiveData<BannerResponse> = MutableLiveData()

    fun query(query: String) {
        viewModelScope.launch {
            LOG.d("UnsplashViewModel", "query ")
            liveQuery.value = repository.search(query)
            LOG.d("UnsplashViewModel", "query  ${liveQuery.value}")
        }
    }

    fun reqBanner() {
        viewModelScope.launch {
            LOG.d("UnsplashViewModel", "reqBanner  ")
            liveBanner.value = repository.reqBanner()
            LOG.d("UnsplashViewModel", "reqBanner  ${liveBanner.value}")
        }
    }

}