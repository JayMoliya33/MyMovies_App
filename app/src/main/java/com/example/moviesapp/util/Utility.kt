package com.example.moviesapp.util

import android.content.Context
import android.widget.Toast

class Utility {

    companion object {

        fun showToast(mContext: Context?, message: String?) {
           val toast = Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show()
        }
    }
}