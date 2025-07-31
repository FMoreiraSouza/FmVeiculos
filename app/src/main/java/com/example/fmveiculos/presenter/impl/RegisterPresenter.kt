package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.model.UserInfoModel
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.presenter.contract.RegisterContract
import com.example.fmveiculos.utils.isValidCPF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val authRepository: AuthRepository
) : RegisterContract.Presenter {

    override fun register(email: String, password: String, cpf: String, name: String, city: String, state: String) {
        if (email.isEmpty() || password.isEmpty() || cpf.isEmpty() || name.isEmpty() || city.isEmpty() || state.isEmpty()) {
            view.showError("Por favor, preencha todos os campos")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError("E-mail inválido")
            return
        }
        if (password.length < 6) {
            view.showError("A senha deve ter pelo menos 6 caracteres")
            return
        }
        if (!isValidCPF(cpf)) {
            view.showError("CPF inválido")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val (success, error) = authRepository.registerUser(email, password, cpf)
            if (success) {
                val userId = authRepository.getCurrentUser()?.uid ?: ""
                val userInfo = UserInfoModel(
                    id = userId, // Usa o UID do Firebase como ID
                    name = name,
                    cpf = cpf,
                    city = city,
                    state = state
                )
                val saveSuccess = authRepository.saveUserInfo(userInfo)
                if (saveSuccess) {
                    view.showSuccess()
                    view.navigateToLogin()
                } else {
                    view.showError("Erro ao salvar informações do usuário")
                }
            } else {
                when (error) {
                    "EMAIL_EXISTS" -> view.showError("E-mail já cadastrado")
                    "CPF_EXISTS" -> view.showError("CPF já cadastrado")
                    "DOMAIN_NOT_ALLOWED" -> view.showError("Clientes não podem ser cadastrados no domínio fmveiculos.com")
                    else -> view.showError("Erro ao cadastrar: $error")
                }
            }
        }
    }
}