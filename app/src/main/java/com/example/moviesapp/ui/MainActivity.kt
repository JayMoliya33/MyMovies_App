package com.example.moviesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviesapp.R
import com.example.moviesapp.base.BaseActivity
import com.example.moviesapp.databinding.ActivityMainBinding
import com.example.moviesapp.repository.MoviesRepository
import com.example.moviesapp.viewModel.MoviesViewModel
import com.example.moviesapp.viewModel.MoviesViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
       setContentView(binding.root)

        initiateUI()
    }

    override fun initiateUI() {

        setUpViewModel()

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.tvShowFragment,
        R.id.favoritesFragment))
        setupActionBarWithNavController(fragment.findNavController(),appBarConfiguration)
        bottomNavigationView.setupWithNavController(fragment.findNavController())

    }

    private fun setUpViewModel(){
        val moviesRepository = MoviesRepository()
        val viewModelProviderFactory = MoviesViewModelProviderFactory(moviesRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MoviesViewModel::class.java)
    }
}