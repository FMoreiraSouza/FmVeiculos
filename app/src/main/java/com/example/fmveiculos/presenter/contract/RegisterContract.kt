package com.example.fmveiculos.presenter.contract

interface RegisterContract {
    interface View {
        fun showError(message: String)
        fun showSuccess()
        fun navigateToLogin()
    }

    interface Presenter {
        fun register(email: String, password: String, cpf: String, name: String, city: String, state: String)
    }
}