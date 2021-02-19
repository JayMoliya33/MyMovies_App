package com.example.moviesapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.movies.MoviesResponse
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MoviesViewModel(
        val moviesRepository: MoviesRepository
) : ViewModel() {

    val popularMoviesdata: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var popularMoviesPage = 1
    var popularMoviesResponse: MoviesResponse? = null

    val topRatedMoviesdata: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var topRatedMoviesPage = 1
    var topRatedMoviesResponse: MoviesResponse? = null

    val upcomingMoviesdata: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var upcomingMoviesPage = 1
    var upcomingMoviesResponse: MoviesResponse? = null

    init {
        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }

     fun getPopularMovies() = viewModelScope.launch {
        popularMoviesdata.postValue(Resource.Loading())
        val response = moviesRepository.getPopularMovies(popularMoviesPage)
        popularMoviesdata.postValue(handlePopularMoviesResponse(response))
    }

     fun getTopRatedMovies() = viewModelScope.launch {
        topRatedMoviesdata.postValue(Resource.Loading())
        val response = moviesRepository.getTopRatedMovies(topRatedMoviesPage)
        topRatedMoviesdata.postValue(handleTopRatedMoviesResponse(response))
    }

     fun getUpcomingMovies() = viewModelScope.launch {
        upcomingMoviesdata.postValue(Resource.Loading())
        val response = moviesRepository.getUpcomingMovies(upcomingMoviesPage)
        upcomingMoviesdata.postValue(handleUpcomingMoviesResponse(response))
    }

    private fun handlePopularMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                popularMoviesPage++
                if (popularMoviesResponse == null) {
                    popularMoviesResponse = resultResponse
                } else {
                    val oldPopularMovies = popularMoviesResponse?.movies
                    val newPopularMovies = resultResponse.movies
                    oldPopularMovies?.addAll(newPopularMovies)
                }
                return Resource.Success(popularMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleTopRatedMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                topRatedMoviesPage++
                if(topRatedMoviesResponse == null){
                    topRatedMoviesResponse = resultResponse
                }else{
                    val oldTopRatedMovies = topRatedMoviesResponse?.movies
                    val newTopRatedMovies = resultResponse.movies
                    oldTopRatedMovies?.addAll(newTopRatedMovies)
                }
                return Resource.Success(topRatedMoviesResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUpcomingMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                upcomingMoviesPage++
                if(upcomingMoviesResponse == null){
                    upcomingMoviesResponse = resultResponse
                }else{
                    val oldUpcomingMovies = upcomingMoviesResponse?.movies
                    val newUpcomingMovies = resultResponse.movies
                    oldUpcomingMovies?.addAll(newUpcomingMovies)
                }
                return Resource.Success(upcomingMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}