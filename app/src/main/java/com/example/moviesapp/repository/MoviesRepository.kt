package com.example.moviesapp.repository

import com.example.moviesapp.api.RetrofitInstance

class MoviesRepository {

    suspend fun getPopularMovies(page : Int) =
        RetrofitInstance.api.getPopularMovies(page)

    suspend fun getTopRatedMovies(page : Int) =
            RetrofitInstance.api.getTopRatedMovies(page)

    suspend fun getUpcomingMovies(page : Int) =
            RetrofitInstance.api.getUpcomingMovies(page)
}