package com.example.fmveiculos.presenter.impl

import android.annotation.SuppressLint
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.presenter.contract.CarDetailsClientContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CarDetailsClientPresenter(
    private val view: CarDetailsClientContract.View,
    private val authRepository: AuthRepository,
    private val carRepository: CarRepository,
    private val interestRepository: InterestRepository
) : CarDetailsClientContract.Presenter {

    @SuppressLint("DefaultLocale")
    override fun loadCarDetails(
        carName: String?,
        carBrand: String?,
        carQuantity: Int,
        carPrice: Double,
        carDescription: String?,
        carCategory: String?,
        carReleaseYear: Int,
        carImageResource: String?
    ) {
        view.displayCarDetails(
            carName = carName,
            carBrand = carBrand,
            carQuantity = carQuantity.toString(),
            carPrice = String.format("R$ %.2f", carPrice),
            carDescription = carDescription,
            carCategory = carCategory,
            carReleaseYear = carReleaseYear.toString(),
            carImageResource = carImageResource
        )
    }

    override fun confirmInterest(userId: String?, carName: String?, carPrice: Double) {
        if (userId == null) {
            view.showInterestError("Usuário não autenticado")
            return
        }
        if (carName.isNullOrEmpty()) {
            view.showInterestError("Nome do carro inválido")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val userInfo = authRepository.getUserInfo(userId)
            if (userInfo == null || userInfo.name.isEmpty()) {
                view.showInterestError("Erro ao obter informações do usuário")
                return@launch
            }

            val car = carRepository.getCarByName(carName)
            if (car == null) {
                view.showInterestError("Carro não encontrado")
                return@launch
            }

            if (car.quantity <= 0) {
                view.showInterestError("Este modelo está indisponível no momento")
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

            val success = interestRepository.saveInterest(interest)
            if (success) {
                view.showInterestSuccess()
            } else {
                view.showInterestError("Erro na solicitação de pedido")
            }
        }
    }

    override fun onWhatsAppClicked() {
        view.openWhatsApp("+5588992696529")
    }

    override fun onBackClicked() {
        view.navigateToHome()
    }
}