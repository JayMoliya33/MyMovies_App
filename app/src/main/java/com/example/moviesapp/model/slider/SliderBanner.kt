package com.example.moviesapp.model.slider

data class SliderBanner(
    val backdrop_path: String,
    val id: Int,
    val name: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
)