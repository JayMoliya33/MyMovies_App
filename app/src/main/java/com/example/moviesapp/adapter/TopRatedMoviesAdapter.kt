package com.example.moviesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ItemTopRatedMoviesBinding
import com.example.moviesapp.model.Movies
import com.example.moviesapp.util.Constants.Companion.IMAGE_BASE_URL
import com.example.moviesapp.util.GlideAppModule

class TopRatedMoviesAdapter : RecyclerView.Adapter<TopRatedMoviesAdapter.MyViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Movies>(){
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this,differCallback)

    inner class MyViewHolder(val binding: ItemTopRatedMoviesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTopRatedMoviesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movieItem = differ.currentList[position]

        holder.binding.apply {
            tvTopRatedMoviesTitle.text = movieItem.title
            GlideAppModule.getRequest(root, GlideAppModule.CacheOptions.Memory)
                    .load(IMAGE_BASE_URL+ movieItem.poster_path)
                    .error(R.drawable.ic_rating_star)
                    .into(ivTopRated)

//            setOnItemClickListener {
//                onItemClickListener?.let {
//                    it(movieItem)
//                }
//            }
        }
    }

    // private var onItemClickListener : ((Movies) -> Unit)? = null

//    fun setOnItemClickListener(listener: (Movies) -> Unit){
//        onItemClickListener = listener
//    }
}