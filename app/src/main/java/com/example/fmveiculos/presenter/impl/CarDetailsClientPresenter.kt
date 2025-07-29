package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.CarDetailsClientContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CarDetailsClientPresenter(
    private val view: CarDetailsClientContract.View,
    private val authRepository: AuthRepository,
    private val interestRepository: InterestRepository,
    private val carRepository: CarRepository
) : CarDetailsClientContract.Presenter {

    override fun confirmInterest(userId: String?, carName: String, carPrice: Double, carQuantity: Int) {
        if (userId == null) {
            view.showInterestError("Usuário não autenticado")
            return
        }
        if (carQuantity <= 0) {
            view.showInterestError("Este modelo está indisponível no momento")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val userInfo = authRepository.getUserInfo(userId)
            if (userInfo == null) {
                view.showInterestError("Erro ao obter informações do usuário")
                return@launch
            }
            val car = carRepository.getCarByName(carName)
            if (car == null) {
                view.showInterestError("Carro não encontrado")
                return@launch
            }
            val interest = InterestModel(
                userId = userId,
                carId = car.id,
                name = userInfo.name,
                carName = carName,
                carPrice = carPrice,
                timestamp = Calendar.getInstance().time.toString(),
                status = "Pendente"
            )
            if (interestRepository.saveInterest(interest)) {
                view.showInterestSaved()
            } else {
                view.showInterestError("Erro na solicitação de pedido")
            }
        }
    }

    override fun onWhatsAppClicked() {
        view.openWhatsApp("+5588992696529")
    }
}