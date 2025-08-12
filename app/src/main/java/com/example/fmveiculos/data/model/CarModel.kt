package com.example.fmveiculos.data.model

import android.content.Context
import android.content.Intent
import com.example.fmveiculos.ui.view.activity.CarDetailsAdminActivity
import com.example.fmveiculos.ui.view.activity.CarDetailsClientActivity

data class CarModel(
    var id: String = "",
    var name: String = "",
    var imageResource: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var brand: String = "",
    var category: String = "",
    var releaseYear: Int = 0,
    var quantity: Int = 0
) {
    companion object {
        fun createIntent(context: Context, car: CarModel, hasWhatsAppLayout: Boolean): Intent {
            return if (!hasWhatsAppLayout) {
                Intent(context, CarDetailsAdminActivity::class.java)
            } else {
                Intent(context, CarDetailsClientActivity::class.java)
            }.apply {
                putExtra("carImage", car.imageResource)
                putExtra("carName", car.name)
                putExtra("carDescription", car.description)
                putExtra("carPrice", car.price)
                putExtra("carCategory", car.category)
                putExtra("carBrand", car.brand)
                putExtra("carReleaseYear", car.releaseYear)
                putExtra("carQuantity", car.quantity)
            }
        }
    }
}