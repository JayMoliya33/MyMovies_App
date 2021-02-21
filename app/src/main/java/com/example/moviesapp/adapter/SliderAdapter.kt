package com.example.moviesapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.moviesapp.R
import com.example.moviesapp.databinding.SliderItemBinding
import com.example.moviesapp.model.slider.SliderBanner
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.GlideAppModule
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private var sliderModelList: MutableList<SliderBanner>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {

        return SliderViewHolder(SliderItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        Log.e("onBind", "inside onbindview")

        holder.binding.apply {
            GlideAppModule.getRequest(root, GlideAppModule.CacheOptions.Memory)
                .load(Constants.IMAGE_BASE_URL + sliderModelList[position].poster_path)
                .error(R.drawable.ic_rating_star)
                .into(sliderImg)
            sliderTitle.text = sliderModelList[position].title
        }

//        holder.itemView.setOnClickListener {
//            Toast.makeText(
//                context,
//                "This is item in position $position",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return 5
    }

    inner class SliderViewHolder(var binding: SliderItemBinding) : ViewHolder(binding.root)

}