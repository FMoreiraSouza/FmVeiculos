package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.presenter.contract.SettingsContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsPresenter(
    private val view: SettingsContract.View,
    private val authRepository: AuthRepository
) : SettingsContract.Presenter {

    override fun loadUserInfo(userId: String?) {
        if (userId == null) {
            view.showError("Usuário não autenticado")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val userInfo = authRepository.getUserInfo(userId)
            if (userInfo != null) {
                view.displayUserInfo(userInfo)
            } else {
                view.showError("Erro ao carregar informações do usuário")
            }
        }
    }
}