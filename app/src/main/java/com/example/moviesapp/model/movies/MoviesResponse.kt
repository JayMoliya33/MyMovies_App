package com.example.moviesapp.model.movies

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MoviesResponse(
        val page: Int,
        @SerializedName("results")
        val movies: MutableList<Movies>,
        val total_pages: Int,
        val total_results: Int
) : Serializable