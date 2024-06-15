package com.example.fmveiculos.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

fun setupUI(dealershipNameView: TextView) {
    val dealershipText = "FMVeículos"
    val spannableDealership = SpannableString(dealershipText)
    spannableDealership.setSpan(
        ForegroundColorSpan(Color.parseColor("#A9A9A9")),
        0,
        2,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableDealership.setSpan(
        ForegroundColorSpan(Color.parseColor("#FF4500")),
        2,
        dealershipText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    dealershipNameView.text = spannableDealership

}