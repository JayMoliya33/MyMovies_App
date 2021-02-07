package com.example.moviesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.MainActivity
import com.example.moviesapp.R
import com.example.moviesapp.adapter.MoviesAdapter
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.util.Resource
import com.example.moviesapp.viewModel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        homeViewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        homeViewModel.moviesdata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        Log.e("success", "success message")
                        moviesAdapter.differ.submitList(moviesResponse.results)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        Log.e("Errorss", "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    Log.e("loading", "inside loading")
                    showProgressBar()
                }
            }
        })

    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        Log.e("recview", "inside setUpRecView()")
        moviesAdapter = MoviesAdapter()
        binding.movieRecyclerView.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
            Log.e("adapter", moviesAdapter.differ.currentList.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}