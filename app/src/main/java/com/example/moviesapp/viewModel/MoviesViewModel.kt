package com.example.moviesapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.MoviesResponse
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MoviesViewModel(
    val moviesRepository: MoviesRepository
) : ViewModel(){

    val moviesdata : MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var moviesPage = 1

    init {
        getPopularMovies()
    }

    private fun getPopularMovies() = viewModelScope.launch {
        moviesdata.postValue(Resource.Loading())
        val response = moviesRepository.getPopularMovies(moviesPage)
        moviesdata.postValue(handleMoviesResponse(response))
    }

    private fun handleMoviesResponse(response : Response<MoviesResponse>) : Resource<MoviesResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}