package com.example.moviesapp.model.slider

import com.google.gson.annotations.SerializedName

data class TrendingMediaSliderResponse(
    val page: Int,
    @SerializedName("results")
    val sliderBanners: List<SliderBanner>,
    val total_pages: Int,
    val total_results: Int
)