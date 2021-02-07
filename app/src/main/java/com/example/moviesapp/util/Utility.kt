package com.example.moviesapp.util

import android.content.Context
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class Utility {

    companion object {

        fun showToast(mContext: Context?, message: String?) {
            val toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}