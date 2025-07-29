package com.example.fmveiculos.presenter.contract

interface CarDetailsClientContract {
    interface View {
        fun showInterestSaved()
        fun showInterestError(message: String)
        fun openWhatsApp(phoneNumber: String)
        fun navigateToHome()
    }

    interface Presenter {
        fun confirmInterest(userId: String?, carName: String, carPrice: Double, carQuantity: Int)
        fun onWhatsAppClicked()
    }
}