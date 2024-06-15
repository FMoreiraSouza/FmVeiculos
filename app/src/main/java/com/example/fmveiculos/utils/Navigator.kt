package com.example.fmveiculos.utils

import android.content.Context
import android.content.Intent

class Navigator {
    fun navigateToActivity(context: Context, activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }
}
