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
            Log.d("CarDetailsDebug", "updateCarQuantity: Iniciando atualização para carName: $carName, quantity: $quantity")
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                Log.d("CarDetailsDebug", "updateCarQuantity: Carro não encontrado para carName: $carName")
                view.showUpdateError("Carro não encontrado")
                return@launch
            }
            Log.d("CarDetailsDebug", "updateCarQuantity: Carro encontrado - id: ${car.id}, name: ${car.name}")
            Log.d("CarDetailsDebug", "updateCarQuantity: Chamando carRepository.updateCarQuantity com carId: ${car.id}, quantity: $quantity")
            if (carRepository.updateCarQuantity(car.id, quantity)) {
                Log.d("CarDetailsDebug", "updateCarQuantity: Atualização de quantidade bem-sucedida")
                view.showQuantityUpdated()
                onUpdate(quantity)
            } else {
                Log.d("CarDetailsDebug", "updateCarQuantity: Falha na atualização de quantidade")
                view.showUpdateError("Erro ao atualizar quantidade")
            }
        }
    }

    override fun updateCarPrice(carName: String, price: Double, onUpdate: (Double) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("CarDetailsDebug", "updateCarPrice: Iniciando atualização para carName: $carName, price: $price")
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                Log.d("CarDetailsDebug", "updateCarPrice: Carro não encontrado para carName: $carName")
                view.showUpdateError("Carro não encontrado")
                return@launch
            }
            Log.d("CarDetailsDebug", "updateCarPrice: Carro encontrado - id: ${car.id}, name: ${car.name}")
            Log.d("CarDetailsDebug", "updateCarPrice: Chamando carRepository.updateCarPrice com carId: ${car.id}, price: $price")
            if (carRepository.updateCarPrice(car.id, price)) {
                Log.d("CarDetailsDebug", "updateCarPrice: Atualização de preço bem-sucedida")
                view.showPriceUpdated()
                onUpdate(price)
            } else {
                Log.d("CarDetailsDebug", "updateCarPrice: Falha na atualização de preço")
                view.showUpdateError("Erro ao atualizar preço")
            }
        }
    }

    override suspend fun getCarByName(carName: String): CarModel? {
        Log.d("CarDetailsDebug", "getCarByName: Iniciando busca para carName: $carName")
        return withContext(Dispatchers.IO) {
            Log.d("CarDetailsDebug", "getCarByName: Chamando carRepository.getCarByName")
            val car = carRepository.getCarByName(carName)
            if (car != null) {
                Log.d("CarDetailsDebug", "getCarByName: Carro encontrado - id: ${car.id}, name: ${car.name}")
            } else {
                Log.d("CarDetailsDebug", "getCarByName: Carro não encontrado para carName: $carName")
            }
            car
        }
    }
}