package com.example.fmveiculos.presenter.impl

import android.util.Log
import com.example.fmveiculos.data.model.CarModel
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.presenter.contract.CarDetailsHomeContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarDetailsAdminPresenter(
    private val view: CarDetailsHomeContract.View,
    private val carRepository: CarRepository
) : CarDetailsHomeContract.Presenter {

    override fun updateCarQuantity(carName: String, quantity: Int, onUpdate: (Int) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                view.showUpdateError("Carro não encontrado")
                return@launch
            }
            if (carRepository.updateCarQuantity(car.id, quantity)) {
                view.showQuantityUpdated()
                onUpdate(quantity)
            } else {
                view.showUpdateError("Erro ao atualizar quantidade")
            }
        }
    }

    override fun updateCarPrice(carName: String, price: Double, onUpdate: (Double) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                view.showUpdateError("Carro não encontrado")
                return@launch
            }
            if (carRepository.updateCarPrice(car.id, price)) {
                view.showPriceUpdated()
                onUpdate(price)
            } else {
                view.showUpdateError("Erro ao atualizar preço")
            }
        }
    }

    override suspend fun getCarByName(carName: String): CarModel? {
        return withContext(Dispatchers.IO) {
            val car = carRepository.getCarByName(carName)
            car
        }
    }
}