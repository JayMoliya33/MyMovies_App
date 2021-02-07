package com.example.moviesapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.databinding.ItemMoviesBinding
import com.example.moviesapp.model.Results

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Results>(){
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this,differCallback)

    inner class MyViewHolder(val binding: ItemMoviesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.e("oncreateview", "inside oncreateview()")
        return MyViewHolder(ItemMoviesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movieItem = differ.currentList[position]

        holder.binding.apply {
            Log.e("onbindview", "inside onbindview()")
            tvMovieName.text = movieItem.title
            textStatus.text = movieItem.vote_average.toString()
            tvNetwork.text = movieItem.release_date
            Glide.with(root).load(movieItem.backdrop_path).into(imageViewMovies)

//            todoItemLayout.setOnClickListener {
//                // pass current Data to Update Fragment
//                val action = HomeFragmentDirections.actionHomeFragmentToUpdateTodoFragment(movieItem)
//                holder.itemView.findNavController().navigate(action)
//            }

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