package com.example.moviesapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.slider.TrendingMediaResponse
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class TrendingMediaViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    val trendingMediaData: MutableLiveData<Resource<TrendingMediaResponse>> = MutableLiveData()
    var trendingMediaPage = 1

    init {
        getTrendingMedia()
    }

    fun getTrendingMedia() = viewModelScope.launch {
        trendingMediaData.postValue(Resource.Loading())
        val response = repository.getTrendingMedia(trendingMediaPage)
        trendingMediaData.postValue(handleTrendingMediaResponse(response))
    }

    private fun handleTrendingMediaResponse(response: Response<TrendingMediaResponse>): Resource<TrendingMediaResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}