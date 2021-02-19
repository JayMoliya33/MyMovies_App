package com.example.moviesapp.ui

import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.adapter.MoviesCategoriesAdapter
import com.example.moviesapp.base.BaseActivity
import com.example.moviesapp.databinding.ActivityViewMoreBinding
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import com.example.moviesapp.viewModel.MoviesViewModelProviderFactory


class ViewMoreActivity : BaseActivity() {

    private lateinit var binding: ActivityViewMoreBinding
    private lateinit var moviesCategoriesAdapter: MoviesCategoriesAdapter
    private lateinit var viewAllMoviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiateUI()
    }

    override fun initiateUI() {

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = intent.getStringExtra("category")
        }

        setUpViewModel()

        when (intent.getStringExtra("category")) {
            Constants.POPULAR ->
                showPopularMovies()
            Constants.TOP_RATED ->
                showTopRatedMovies()
            Constants.UPCOMING ->
                showUpcomingMovies()
        }
    }

    private fun showPopularMovies() {
        setUpRecyclerView()
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
        binding.recViewViewAllMovies.addOnScrollListener(this@ViewMoreActivity.scrollListenerPopularMovies)
    }

    private fun showTopRatedMovies() {
        setUpRecyclerView()
        viewAllMoviesViewModel.topRatedMoviesdata.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        moviesCategoriesAdapter.differ.submitList(moviesResponse.movies.toList())
                        val totalPage = moviesResponse.total_results / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewAllMoviesViewModel.topRatedMoviesPage == totalPage
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
        binding.recViewViewAllMovies.addOnScrollListener(this@ViewMoreActivity.scrollListenerTopRatedMovies)
    }

    private fun showUpcomingMovies() {
        setUpRecyclerView()
        viewAllMoviesViewModel.upcomingMoviesdata.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        moviesCategoriesAdapter.differ.submitList(moviesResponse.movies.toList())
                        val totalPage = moviesResponse.total_results / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewAllMoviesViewModel.upcomingMoviesPage == totalPage
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
        binding.recViewViewAllMovies.addOnScrollListener(this@ViewMoreActivity.scrollListenerUpcomingMovies)
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    /*
        popularMovies OnScrollListener
     */
    private val scrollListenerPopularMovies = object : RecyclerView.OnScrollListener() {

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

    /*
       topRatedMovies OnScrollListener
    */
    private val scrollListenerTopRatedMovies = object : RecyclerView.OnScrollListener() {

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
                viewAllMoviesViewModel.getTopRatedMovies()
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

    /*
      upcomingMovies OnScrollListener
   */
    private val scrollListenerUpcomingMovies = object : RecyclerView.OnScrollListener() {

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
                viewAllMoviesViewModel.getUpcomingMovies()
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

    private fun setUpRecyclerView() {
        moviesCategoriesAdapter = MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_GRID)
        binding.recViewViewAllMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    private fun setUpViewModel() {
        val moviesRepository = MoviesRepository()
        val viewModelProviderFactory = MoviesViewModelProviderFactory(moviesRepository)
        viewAllMoviesViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            MoviesViewModel::class.java
        )
    }

    private fun showProgressBar() {
        binding.progressBarViewMore.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        binding.progressBarViewMore.visibility = View.INVISIBLE
        isLoading = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.home) {
            finish()
        }

        return true
    }
}
