package com.example.fmveiculos.presenter.contract

interface CarDetailsClientContract {
    interface View {
        fun displayCarDetails(
            carName: String?,
            carBrand: String?,
            carQuantity: String,
            carPrice: String,
            carDescription: String?,
            carCategory: String?,
            carReleaseYear: String,
            carImageResource: String?
        )
        fun showInterestSuccess()
        fun showInterestError(message: String)
        fun showWhatsAppError()
        fun navigateToHome()
        fun openWhatsApp(phoneNumber: String)
    }

    interface Presenter {
        fun loadCarDetails(
            carName: String?,
            carBrand: String?,
            carQuantity: Int,
            carPrice: Double,
            carDescription: String?,
            carCategory: String?,
            carReleaseYear: Int,
            carImageResource: String?
        )
        fun confirmInterest(userId: String?, carName: String?, carPrice: Double)
        fun onWhatsAppClicked()
        fun onBackClicked()
    }
}