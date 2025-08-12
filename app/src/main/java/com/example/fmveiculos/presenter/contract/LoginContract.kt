package com.example.fmveiculos.presenter.contract

interface LoginContract {
    interface View {
        fun showEmailError(message: String)
        fun clearEmailError()
        fun showPasswordError(message: String)
        fun clearPasswordError()
        fun showLoginSuccess()
        fun showLoginError(message: String)
        fun navigateToHome(isAdmin: Boolean)
        fun navigateToForgotPassword()
        fun navigateToRegister()
    }

    interface Presenter {
        fun login(email: String, password: String)
        fun checkLoggedUser()
        fun onForgotPasswordClicked()
        fun onRegisterClicked()
    }
}