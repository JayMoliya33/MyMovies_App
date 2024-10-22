package com.example.moviesapp.model.movies

data class Movies(
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    var popularity: Double?,
    var poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double,
    val vote_count: Int?
)