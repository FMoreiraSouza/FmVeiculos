package com.example.fmveiculos.presenter.impl

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.fmveiculos.data.model.CarModel
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.presenter.contract.RestockingContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestockingPresenter(
    private val view: RestockingContract.View,
    private val carRepository: CarRepository
) : RestockingContract.Presenter {

    private var uriSelectedImage: Uri? = null
    private var bitmapSelectedImage: Bitmap? = null

    override fun setSelectedImage(uri: Uri?) {
        Log.d("RestockingDebug", "setSelectedImage: Definindo URI - uri: ${uri?.toString() ?: "null"}")
        uriSelectedImage = uri
        if (uri != null) {
            view.showImageSelected()
        } else {
            view.showNoImageSelected()
        }
    }

    override fun setBitmapImage(bitmap: Bitmap?) {
        Log.d("RestockingDebug", "setBitmapImage: Definindo bitmap - bitmap: ${if (bitmap != null) "presente" else "null"}")
        bitmapSelectedImage = bitmap
        if (bitmap != null) {
            view.showImageSelected()
        } else {
            view.showNoImageSelected()
        }
    }

    override fun restoreDefaultImage() {
        Log.d("RestockingDebug", "restoreDefaultImage: Restaurando imagem padrão")
        view.showNoImageSelected()
    }

    override fun saveCar(car: CarModel) {
        Log.d("RestockingDebug", "saveCar: Iniciando salvamento do carro: ${car.name}")
        if (uriSelectedImage == null && bitmapSelectedImage == null) {
            Log.d("RestockingDebug", "saveCar: Nenhuma imagem selecionada")
            view.showCarSavedError("Por favor, selecione uma imagem")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("RestockingDebug", "saveCar: Chamando carRepository.uploadImageAndSaveCar")
            val success = carRepository.uploadImageAndSaveCar(car, uriSelectedImage, bitmapSelectedImage)
            if (success) {
                Log.d("RestockingDebug", "saveCar: Carro salvo com sucesso")
                view.showCarSavedSuccess()
            } else {
                Log.d("RestockingDebug", "saveCar: Erro ao salvar carro")
                view.showCarSavedError("Erro ao salvar carro")
            }
        }
    }
}