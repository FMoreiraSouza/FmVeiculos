import android.content.Context
import android.content.Intent
import com.example.fmveiculos.ui.view.dealership.CarDetailsClientActivity
import com.example.fmveiculos.ui.view.dealership.CarDetailsHomeActivity

data class CarModel(
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
                Intent(context, CarDetailsHomeActivity::class.java)
            } else {
                Intent(context, CarDetailsClientActivity::class.java)
            }.apply {
                // Adiciona os dados do carro ao intent
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
