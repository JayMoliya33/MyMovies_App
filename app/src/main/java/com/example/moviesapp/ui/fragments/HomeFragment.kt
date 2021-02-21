package com.example.moviesapp.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesapp.R
import com.example.moviesapp.adapter.MoviesCategoriesAdapter
import com.example.moviesapp.adapter.SliderAdapter
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.ui.ViewMoreActivity
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: MoviesViewModel
    private lateinit var moviesCategoriesAdapter: MoviesCategoriesAdapter
    private lateinit var sliderAdapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initiateUI()
    }

    private fun initiateUI() {
        homeViewModel = (activity as MainActivity).viewModel

        setUpPopularMovies()
        setUpTopRatedMovies()
        setUpUpcomingMovies()

        setUpSlider()

        binding.moviesCategoriesLayout.btnShowMorePopularMovies.setOnClickListener {
            viewAllItemFragment(Constants.POPULAR)
        }

        binding.moviesCategoriesLayout.btnShowMoreTopRatedMovies.setOnClickListener {
            viewAllItemFragment(Constants.TOP_RATED)
        }

        binding.moviesCategoriesLayout.btnShowMoreUpcomingMovies.setOnClickListener {
            viewAllItemFragment(Constants.UPCOMING)
        }
    }

    private fun setUpSlider() {
        homeViewModel.trendingMediaData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { mediaResponse ->
                        sliderAdapter = SliderAdapter(mediaResponse.sliderBanners)
                        binding.slider.setSliderAdapter(sliderAdapter)
                        //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        binding.slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        binding.slider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
                        binding.slider.indicatorSelectedColor = Color.WHITE;
                        binding.slider.indicatorUnselectedColor = Color.GRAY;
                        binding.slider.scrollTimeInSec = 4; //set scroll delay in seconds :
                        binding.slider.startAutoCycle();
//                        binding.slider.apply {
//                            adapter = sliderAdapter
//                        }
                        Log.e("viewpager", mediaResponse.total_results.toString())
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        Log.e("Errors", "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    Log.e("loading", "inside loading")
                    showProgressBar()
                }
            }
        })
    }

    /*
     call api from viewmodel
     */
    private fun setUpUpcomingMovies() {
        homeViewModel.upcomingMoviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        setUpcomingMoviesRecyclerView()
                        Log.e("success", "success message")
                        moviesCategoriesAdapter.differ.submitList(moviesResponse.movies)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        Log.e("Errors", "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    Log.e("loading", "inside loading")
                    showProgressBar()
                }
            }
        })
    }

    /*
       call api from viewmodel
    */
    private fun setUpTopRatedMovies() {
        homeViewModel.topRatedMoviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        setUpTopRatedMoviesRecyclerView()
                        Log.e("success", "success message")
                        moviesCategoriesAdapter.differ.submitList(moviesResponse.movies)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        Log.e("Errors", "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    Log.e("loading", "inside loading")
                    showProgressBar()
                }
            }
        })
    }

    /*
       call api from viewmodel
     */
    private fun setUpPopularMovies() {
        homeViewModel.popularMoviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        setUpPopularMoviesRecyclerView()
                        moviesCategoriesAdapter.differ.submitList(moviesResponse.movies.toList())
//                        val intent = Intent(activity, ViewMoreActivity::class.java)
//                       intent.put("Items", moviesResponse.movies.toList())
//                        startActivity(intent)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        Log.e("Errors", "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    Log.e("loading", "inside loading")
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setUpPopularMoviesRecyclerView() {
        moviesCategoriesAdapter =
            MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_HORIZONTAL)
        binding.moviesCategoriesLayout.recViewPopularMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpTopRatedMoviesRecyclerView() {
        moviesCategoriesAdapter =
            MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_HORIZONTAL)
        binding.moviesCategoriesLayout.recViewTopRatedMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpcomingMoviesRecyclerView() {
        moviesCategoriesAdapter =
            MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_HORIZONTAL)
        binding.moviesCategoriesLayout.recViewUpcomingMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun viewAllItemFragment(category: String) {

        val intent = Intent(activity, ViewMoreActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}