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
        uriSelectedImage = uri
        if (uri != null) {
            view.showImageSelected()
        } else {
            view.showNoImageSelected()
        }
    }

    override fun setBitmapImage(bitmap: Bitmap?) {
        bitmapSelectedImage = bitmap
        if (bitmap != null) {
            view.showImageSelected()
        } else {
            view.showNoImageSelected()
        }
    }

    override fun restoreDefaultImage() {
        view.showNoImageSelected()
    }

    override fun saveCar(car: CarModel) {
        if (uriSelectedImage == null && bitmapSelectedImage == null) {
            view.showCarSavedError("Por favor, selecione uma imagem")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val success = carRepository.uploadImageAndSaveCar(car, uriSelectedImage, bitmapSelectedImage)
            if (success) {
                view.showCarSavedSuccess()
            } else {
                view.showCarSavedError("Erro ao salvar carro")
            }
        }
    }
}