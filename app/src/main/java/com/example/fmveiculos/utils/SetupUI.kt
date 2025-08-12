package com.example.fmveiculos.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.graphics.toColorInt

fun setupUI(dealershipNameView: TextView) {
    val dealershipText = "FMVeículos"
    val spannableDealership = SpannableString(dealershipText)
    spannableDealership.setSpan(
        ForegroundColorSpan("#A9A9A9".toColorInt()),
        0,
        2,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableDealership.setSpan(
        ForegroundColorSpan("#FF4500".toColorInt()),
        2,
        dealershipText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    dealershipNameView.text = spannableDealership
}