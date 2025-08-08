package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.model.CarModel
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.presenter.contract.CarDetailsHomeContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsAdminPresenter(
    private val view: CarDetailsHomeContract.View,
    private val carRepository: CarRepository
) : CarDetailsHomeContract.Presenter {

    override fun updateCarQuantity(carName: String, quantity: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                view.showUpdateError("Carro não encontrado")
                return@launch
            }
            if (carRepository.updateCarQuantity(car.id, quantity)) {
                view.showQuantityUpdated()
            } else {
                view.showUpdateError("Erro ao atualizar quantidade")
            }
        }
    }

    override fun updateCarPrice(carName: String, price: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                view.showUpdateError("Carro não encontrado")
                return@launch
            }
            if (carRepository.updateCarPrice(car.id, price)) {
                view.showPriceUpdated()
            } else {
                view.showUpdateError("Erro ao atualizar preço")
            }
        }
    }

    override fun getCarByName(carName: String): CarModel? {
        var car: CarModel? = null
        CoroutineScope(Dispatchers.Main).launch {
            car = carRepository.getCarByName(carName)
        }
        return car
    }
}