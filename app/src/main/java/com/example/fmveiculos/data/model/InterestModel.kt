package com.example.fmveiculos.data.model

data class InterestModel(
    val id: String = "",
    val userId: String = "",
    val carId: String = "",
    val name: String = "",
    val carName: String = "",
    val carPrice: Double = 0.0,
    val timestamp: String = "",
    var status: String = ""
)