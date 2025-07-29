package com.example.fmveiculos.presenter.contract

import android.graphics.Bitmap
import android.net.Uri
import com.example.fmveiculos.data.model.CarModel

interface RestockingContract {
    interface View {
        fun showImageSelected()
        fun showNoImageSelected()
        fun showCarSavedSuccess()
        fun showCarSavedError(message: String)
        fun navigateToHome()
    }

    interface Presenter {
        fun setSelectedImage(uri: Uri?)
        fun setBitmapImage(bitmap: Bitmap?)
        fun saveCar(car: CarModel)
    }
}