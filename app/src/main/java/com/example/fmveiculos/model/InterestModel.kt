package com.example.fmveiculos.model

data class InterestModel(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val carName: String = "",
    val carPrice: Double = 0.0,
    val timestamp: String = "",
    var status: String = ""
)
