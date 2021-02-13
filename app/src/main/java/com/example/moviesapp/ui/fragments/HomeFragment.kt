package com.example.moviesapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.adapter.PopularMoviesAdapter
import com.example.moviesapp.adapter.TopRatedMoviesAdapter
import com.example.moviesapp.adapter.UpcomingMoviesAdapter
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: MoviesViewModel

    private lateinit var moviesAdapter: PopularMoviesAdapter
    private lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter
    private lateinit var upcomingMoviesAdapter: UpcomingMoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        homeViewModel = (activity as MainActivity).viewModel

        setUpPopularMoviesRecyclerView()
        setUpTopRatedMoviesRecyclerView()
        setUpcomingMoviesRecyclerView()
        homeViewModel.popularMoviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        moviesAdapter.differ.submitList(moviesResponse.movies.toList())
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

        homeViewModel.topRatedMoviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        topRatedMoviesAdapter.differ.submitList(moviesResponse.movies)
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

        homeViewModel.upcomingMoviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        upcomingMoviesAdapter.differ.submitList(moviesResponse.movies)
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

        binding.btnShowMore.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_viewAllFragment)
        }

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setUpPopularMoviesRecyclerView() {
        moviesAdapter = PopularMoviesAdapter(PopularMoviesAdapter.VIEW_TYPE_HORIZONTAL)
        binding.recViewPopularMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpTopRatedMoviesRecyclerView() {
        topRatedMoviesAdapter = TopRatedMoviesAdapter()
        binding.recViewTopRatedMovies.apply {
            adapter = topRatedMoviesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpcomingMoviesRecyclerView() {
        upcomingMoviesAdapter = UpcomingMoviesAdapter()
        binding.recViewUpcomingMovies.apply {
            adapter = upcomingMoviesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}