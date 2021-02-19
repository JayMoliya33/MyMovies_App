package com.example.moviesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ItemMoviesCategoriesBinding
import com.example.moviesapp.databinding.ItemMoviesCategoriesGridBinding
import com.example.moviesapp.model.movies.Movies
import com.example.moviesapp.util.Constants.Companion.IMAGE_BASE_URL
import com.example.moviesapp.util.GlideAppModule

class MoviesCategoriesAdapter(var viewType : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    inner class HorizontalMoviesViewHolder(val binding: ItemMoviesCategoriesBinding) : RecyclerView.ViewHolder(binding.root)

    inner class GridMoviesViewHolder(val binding: ItemMoviesCategoriesGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_HORIZONTAL) {
            HorizontalMoviesViewHolder(ItemMoviesCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            GridMoviesViewHolder(ItemMoviesCategoriesGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieItem = differ.currentList[position]

        if (holder is HorizontalMoviesViewHolder) {
            holder.binding.apply {
                tvMovieTitle.text = movieItem.title
                GlideAppModule.getRequest(root, GlideAppModule.CacheOptions.Memory)
                        .load(IMAGE_BASE_URL + movieItem.poster_path)
                        .error(R.drawable.ic_rating_star)
                        .into(ivMovies)
            }
        } else if (holder is GridMoviesViewHolder) {
            holder.binding.apply {
                tvPopularMovieTitle.text = movieItem.title
                GlideAppModule.getRequest(root, GlideAppModule.CacheOptions.Memory)
                        .load(IMAGE_BASE_URL + movieItem.poster_path)
                        .error(R.drawable.ic_rating_star)
                        .into(ivPopularMovies)
            }

        }

        // private var onItemClickListener : ((Movies) -> Unit)? = null

//    fun setOnItemClickListener(listener: (Movies) -> Unit){
//        onItemClickListener = listener
//    }

    }
    companion object {
        var VIEW_TYPE_HORIZONTAL = 1
        var VIEW_TYPE_GRID = 2
    }


}