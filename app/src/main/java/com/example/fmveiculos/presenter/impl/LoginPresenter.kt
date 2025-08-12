package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.presenter.contract.LoginContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginPresenter(
    private val view: LoginContract.View,
    private val authRepository: AuthRepository
) : LoginContract.Presenter {

    override fun login(email: String, password: String) {
        if (email.isEmpty()) {
            view.showEmailError("Email vazio")
            return
        }
        view.clearEmailError()
        if (password.isEmpty()) {
            view.showPasswordError("Senha vazia")
            return
        }
        view.clearPasswordError()

        CoroutineScope(Dispatchers.Main).launch {
            val success = authRepository.loginUser(email, password)
            if (success) {
                view.showLoginSuccess()
                val isAdmin = authRepository.getCurrentUserEmail()?.endsWith("@fmveiculos.com") ?: false
                view.navigateToHome(isAdmin)
            } else {
                view.showLoginError("Email ou senha inválidos")
            }
        }
    }

    override fun checkLoggedUser() {
        if (authRepository.checkLoggedUser()) {
            val isAdmin = authRepository.getCurrentUserEmail()?.endsWith("@fmveiculos.com") ?: false
            view.navigateToHome(isAdmin)
        }
    }

    override fun onForgotPasswordClicked() {
        view.navigateToForgotPassword()
    }

    override fun onRegisterClicked() {
        view.navigateToRegister()
    }
}