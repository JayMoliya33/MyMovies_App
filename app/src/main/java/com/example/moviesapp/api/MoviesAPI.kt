package com.example.moviesapp.api

import com.example.moviesapp.model.movies.MoviesResponse
import com.example.moviesapp.model.slider.TrendingMediaSliderResponse
import com.example.moviesapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET(ApiUtils.POPULAR)
    suspend fun getPopularMovies(
            @Query("page")
            pageNumber: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
    ): Response<MoviesResponse>

    @GET(ApiUtils.TOP_RATED)
    suspend fun getTopRatedMovies(
            @Query("page")
            pageNumber: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("region")
            region: String = "IN"
    ): Response<MoviesResponse>

    @GET(ApiUtils.UPCOMING)
    suspend fun getUpcomingMovies(
            @Query("page")
            pageNumber: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY
    ): Response<MoviesResponse>

    @GET(ApiUtils.TRENDING)
    suspend fun getTrendingMedia(
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY,
    ): Response<TrendingMediaSliderResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Int): Response<MoviesResponse>

    @GET("search/movie")
    fun searchMovie(@Query("query") query: String, @Query("page") page: Int): Response<MoviesResponse>
}