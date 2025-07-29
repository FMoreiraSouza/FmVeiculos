package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.HistoricAdminContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoricAdminPresenter(
    private val view: HistoricAdminContract.View,
    private val interestRepository: InterestRepository
) : HistoricAdminContract.Presenter {

    override fun loadInterests() {
        CoroutineScope(Dispatchers.Main).launch {
            val interests = interestRepository.getAllInterests()
            view.displayInterests(interests)
        }
    }
}