package com.example.moviesapp.repository

import com.example.moviesapp.api.RetrofitInstance

class MoviesRepository {

    suspend fun getPopularMovies(page : Int) =
        RetrofitInstance.api.getPopularMovies(page)
}