package com.example.moviesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.moviesapp.R
import com.example.moviesapp.model.slider.SliderBanner
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.GlideAppModule
import kotlinx.android.synthetic.main.slider_item.view.*

class SliderAdapter(private val sliderModelList: List<SliderBanner>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.slider_item, container, false)

        GlideAppModule.getRequest(view, GlideAppModule.CacheOptions.Memory)
            .load(Constants.IMAGE_BASE_URL + sliderModelList[position].poster_path)
            .error(R.drawable.ic_rating_star)
            .into(view.slider_img)
        view.slider_title.text = sliderModelList[position].title
        container.addView(view,0)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return sliderModelList.size
    }
}