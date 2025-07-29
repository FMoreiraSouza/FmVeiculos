package com.example.fmveiculos.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle

class Navigator {
    fun navigateToActivity(context: Context, activityClass: Class<*>, extras: Bundle? = null) {
        val intent = Intent(context, activityClass)
        extras?.let { intent.putExtras(it) }
        context.startActivity(intent)
    }
}