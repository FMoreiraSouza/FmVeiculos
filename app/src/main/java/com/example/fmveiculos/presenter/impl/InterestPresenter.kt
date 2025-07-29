package com.example.fmveiculos.presenter.impl

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
            val interests = interestRepository.getPendingInterests()
            view.displayInterests(interests)
        }
    }

    override fun confirmInterest(interest: InterestModel) {
        CoroutineScope(Dispatchers.Main).launch {
            if (interestRepository.confirmInterest(interest)) {
                view.showConfirmSuccess()
                loadPendingInterests()
            } else {
                view.showError("Erro ao confirmar interesse ou quantidade insuficiente")
            }
        }
    }

    override fun cancelInterest(interestId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            if (interestRepository.cancelInterest(interestId)) {
                view.showCancelSuccess()
                loadPendingInterests()
            } else {
                view.showError("Erro ao cancelar interesse")
            }
        }
    }
}