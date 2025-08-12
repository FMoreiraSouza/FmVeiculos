package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.HistoricClientContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoricClientPresenter(
    private val view: HistoricClientContract.View,
    private val interestRepository: InterestRepository
) : HistoricClientContract.Presenter {

    override fun loadInterests(userId: String?) {
        if (userId == null) {
            view.showError("Usuário não autenticado")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val interests = interestRepository.getInterestsByUser(userId)
            view.displayInterests(interests)
        }
    }
}