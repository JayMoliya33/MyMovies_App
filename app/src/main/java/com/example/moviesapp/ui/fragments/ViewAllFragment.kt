package com.example.moviesapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.adapter.PopularMoviesAdapter
import com.example.moviesapp.databinding.FragmentViewAllBinding
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class ViewAllFragment : Fragment(R.layout.fragment_view_all) {

    private var _binding: FragmentViewAllBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewAllpopularMoviesViewModel: MoviesViewModel

    private lateinit var moviesAdapter: PopularMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewAllpopularMoviesViewModel = (activity as MainActivity).viewModel

        setUpPopularMoviesRecyclerView()
        viewAllpopularMoviesViewModel.popularMoviesdata.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        moviesAdapter.differ.submitList(moviesResponse.movies.toList())
                        val totalPage = moviesResponse.total_results / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewAllpopularMoviesViewModel.popularMoviesPage == totalPage
                        if (isLastPage) {
                            binding.recViewViewAllPopularMovies.setPadding(0, 0, 0, 0)
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

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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
            val isTotalMoreThanVisible = totalItemCount>= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewAllpopularMoviesViewModel.getPopularMovies()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }

        }

    }

    private fun setUpPopularMoviesRecyclerView() {
        moviesAdapter = PopularMoviesAdapter(PopularMoviesAdapter.VIEW_TYPE_GRID)
        binding.recViewViewAllPopularMovies.apply {
            adapter = moviesAdapter
           // layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

            layoutManager = GridLayoutManager(activity, 3)
            addOnScrollListener(this@ViewAllFragment.scrollListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}