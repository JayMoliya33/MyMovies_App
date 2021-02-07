package com.example.moviesapp.model

data class MoviesResponse(
    val page: Int,
    val results: List<Results>,
    val total_pages: Int,
    val total_results: Int
)