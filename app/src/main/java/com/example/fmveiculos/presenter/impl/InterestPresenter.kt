package com.example.fmveiculos.presenter.impl

import android.util.Log
import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.InterestContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InterestPresenter(
    private val view: InterestContract.View,
    private val interestRepository: InterestRepository
) : InterestContract.Presenter {

    override fun loadPendingInterests() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val interests = interestRepository.getPendingInterests()
                view.displayInterests(interests)
            } catch (_: Exception) {
                view.showError("Erro ao carregar interesses")
            }
        }
    }

    override fun confirmInterest(interest: InterestModel) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val success = interestRepository.confirmInterest(interest)
                if (success) {
                    loadPendingInterests()
                    view.showConfirmSuccess()
                } else {
                    view.showError("Quantidade insuficiente do veículo")
                }
            } catch (_: Exception) {
                view.showError("Erro ao confirmar interesse")
            }
        }
    }

    override fun cancelInterest(interestId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val success = interestRepository.cancelInterest(interestId)
                if (success) {
                    loadPendingInterests()
                    view.showCancelSuccess()
                } else {
                    view.showError("Erro ao cancelar interesse")
                }
            } catch (_: Exception) {
                view.showError("Erro ao cancelar interesse")
            }
        }
    }
}