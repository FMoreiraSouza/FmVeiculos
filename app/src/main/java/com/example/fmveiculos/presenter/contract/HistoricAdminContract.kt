package com.example.fmveiculos.presenter.contract

import com.example.fmveiculos.data.model.InterestModel

interface HistoricAdminContract {
    interface View {
        fun displayInterests(interests: List<InterestModel>)
        fun showError(message: String)
        fun navigateToHome()
    }

    interface Presenter {
        fun loadInterests()
    }
}