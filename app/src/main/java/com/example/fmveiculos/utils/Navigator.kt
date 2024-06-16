package com.example.fmveiculos.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat

class Navigator {
    fun navigateToActivity(context: Context, destination: Class<*>, extras: Bundle? = null) {
        val intent = Intent(context, destination)
        extras?.let {
            intent.putExtras(it)
        }
        ContextCompat.startActivity(context, intent, null)
    }
}
