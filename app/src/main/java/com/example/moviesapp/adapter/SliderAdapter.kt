package com.example.moviesapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ItemMoviesCategoriesBinding
import com.example.moviesapp.databinding.ItemMoviesCategoriesGridBinding
import com.example.moviesapp.databinding.SliderItemBinding
import com.example.moviesapp.model.movies.Movies
import com.example.moviesapp.model.slider.SliderBanner
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.GlideAppModule
import kotlinx.android.synthetic.main.slider_item.view.*

class SliderAdapter(private var sliderModelList: List<SliderBanner>) :
    RecyclerView.Adapter<SliderAdapter.HorizontalMoviesViewHolder>() {

    inner class HorizontalMoviesViewHolder(var binding: SliderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalMoviesViewHolder {

        return HorizontalMoviesViewHolder(
            SliderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = sliderModelList.size

    override fun onBindViewHolder(holder: HorizontalMoviesViewHolder, position: Int) {
        Log.e("onBind", "inside onbindview")

        holder.binding.apply {
            GlideAppModule.getRequest(root, GlideAppModule.CacheOptions.Memory)
                .load(Constants.IMAGE_BASE_URL + sliderModelList[position].poster_path)
                .error(R.drawable.ic_rating_star)
                .into(sliderImg)
            sliderTitle.text = sliderModelList[position].title
        }
    }

}