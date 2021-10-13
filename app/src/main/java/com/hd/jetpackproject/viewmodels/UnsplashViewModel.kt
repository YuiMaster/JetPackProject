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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnsplashViewModel @Inject internal constructor(private val repository: UnsplashRepository) : ViewModel() {
    val liveQuery: MutableLiveData<UnsplashSearchResponse> = MutableLiveData()
    val liveBanner: MutableLiveData<BannerResponse> = MutableLiveData()

    fun query(query: String) {
        viewModelScope.launch {
            liveQuery.value = repository.search(query).asLiveData().value
        }
    }

    fun reqBanner() {
        viewModelScope.launch {
            LOG.d("UnsplashViewModel", "reqBanner $repository")
            liveBanner.value = repository.reqBanner()
            LOG.d("UnsplashViewModel", "reqBanner collect ${liveBanner.value}")
//                .collect {
//                liveBanner.value = it
//                LOG.d("UnsplashViewModel", "reqBanner collect $it")
//            }
//            liveBanner.value = repository.reqBanner().asLiveData().value
        }
    }

}