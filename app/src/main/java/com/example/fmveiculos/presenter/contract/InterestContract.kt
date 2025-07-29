package com.example.fmveiculos.presenter.contract

import com.example.fmveiculos.data.model.InterestModel

interface InterestContract {
    interface View {
        fun displayInterests(interests: List<InterestModel>)
        fun showConfirmSuccess()
        fun showCancelSuccess()
        fun showError(message: String)
        fun navigateToHome()
    }

    interface Presenter {
        fun loadPendingInterests()
        fun confirmInterest(interest: InterestModel)
        fun cancelInterest(interestId: String)
    }
}