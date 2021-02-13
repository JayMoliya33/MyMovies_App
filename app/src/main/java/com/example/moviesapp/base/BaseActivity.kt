package com.example.moviesapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected abstract fun initiateUI()

    override fun onDestroy() {
        super.onDestroy()
    }



}