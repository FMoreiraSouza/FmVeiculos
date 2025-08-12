package com.example.fmveiculos.presenter.contract

interface VehiclesContract {
    interface View {
        fun navigateToHome()
        fun showError(message: String)
    }
}