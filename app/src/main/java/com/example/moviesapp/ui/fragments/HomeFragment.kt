package com.example.moviesapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.moviesapp.R
import com.example.moviesapp.adapter.MoviesCategoriesAdapter
import com.example.moviesapp.api.ApiUtils
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.ui.ViewMoreActivity
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: MoviesViewModel
    private lateinit var moviesCategoriesAdapter: MoviesCategoriesAdapter

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

        homeViewModel = (activity as MainActivity).viewModel

        setUpPopularMovies()
        setUpTopRatedMovies()
        setUpUpcomingMovies()

      //  binding.categoriesMovies.btnShowMore.setOnClickListener(this)
        binding.btnShowMore.setOnClickListener{
            viewAllItemFragment("POPULAR")
        }

//        binding.categoriesMovies.btnShowMore.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_viewAllFragment)
//        }

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
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recViewPopularMovies)
        binding.recViewPopularMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpTopRatedMoviesRecyclerView() {
        moviesCategoriesAdapter =
            MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_HORIZONTAL)
        binding.recViewTopRatedMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpcomingMoviesRecyclerView() {
        moviesCategoriesAdapter =
            MoviesCategoriesAdapter(MoviesCategoriesAdapter.VIEW_TYPE_HORIZONTAL)
        binding.recViewUpcomingMovies.apply {
            adapter = moviesCategoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun viewAllItemFragment(value: String) {
//        val fragment: Fragment = ViewAllFragment()
//        val bundle = Bundle()
//        bundle.putString("value", value)
        // fragment.arguments = bundle
        // findNavController().navigate(R.id.action_homeFragment_to_viewAllFragment)

        Log.e("intent", "inside intent")
        val intent = Intent(activity, ViewMoreActivity::class.java)
        intent.putExtra("category", value)
        startActivity(intent)
    }
}