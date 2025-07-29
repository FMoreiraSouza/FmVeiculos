package com.example.fmveiculos.presenter.contract

import com.example.fmveiculos.data.model.CarModel

interface CarDetailsHomeContract {
    interface View {
        fun showQuantityUpdated()
        fun showPriceUpdated()
        fun showUpdateError(message: String)
        fun navigateToVehicles()
    }

    interface Presenter {
        fun updateCarQuantity(carName: String, quantity: Int)
        fun updateCarPrice(carName: String, price: Double)
        fun getCarByName(carName: String): CarModel?
    }
}