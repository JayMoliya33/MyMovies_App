package com.example.moviesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.adapter.MoviesCategoriesAdapter
import com.example.moviesapp.databinding.ActivityMainBinding
import com.example.moviesapp.databinding.ActivityViewMoreBinding
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import com.example.moviesapp.viewModel.MoviesViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_view_more.*
import kotlinx.android.synthetic.main.fragment_home.*

class ViewMoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewMoreBinding
    private lateinit var moviesCategoriesAdapter: MoviesCategoriesAdapter
    private lateinit var viewAllMoviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityViewMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  viewAllMoviesViewModel = (applicationContext as MainActivity).viewModel
        val moviesRepository = MoviesRepository()
        val viewModelProviderFactory = MoviesViewModelProviderFactory(moviesRepository)
        viewAllMoviesViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MoviesViewModel::class.java)

        val intent = intent
        val category = intent.getStringExtra("category")

        when (category) {
            "POPULAR" ->
                showPopularMovies()
        }

    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun showPopularMovies() {

        setUpPopularMoviesRecyclerView()
        viewAllMoviesViewModel.popularMoviesdata.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        moviesCategoriesAdapter.differ.submitList(moviesResponse.movies.toList())
                        val totalPage = moviesResponse.total_results / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewAllMoviesViewModel.popularMoviesPage == totalPage
                        if (isLastPage) {
                            binding.recViewViewAllMovies.setPadding(0, 0, 0, 0)
                        }
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

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewAllMoviesViewModel.getPopularMovies()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    private fun showProgressBar() {
        progressBarViewMore.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        progressBarViewMore.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun setUpPopularMoviesRecyclerView() {
        moviesCategoriesAdapter =
            MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_GRID)
        binding.recViewViewAllMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = GridLayoutManager(context, 3)
            addOnScrollListener(this@ViewMoreActivity.scrollListener)
        }
    }
}
