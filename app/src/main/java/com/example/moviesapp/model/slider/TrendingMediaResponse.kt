package com.example.moviesapp.model.slider

import com.google.gson.annotations.SerializedName

data class TrendingMediaResponse(
    val page: Int,
    @SerializedName("results")
    val sliderBanners: MutableList<SliderBanner>,
    val total_pages: Int,
    val total_results: Int
)