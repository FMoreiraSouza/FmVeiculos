import android.content.Context
import android.content.Intent
import com.example.fmveiculos.view.dealership.CarDetailsActivity

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
        fun createIntent(context: Context, car: CarModel): Intent {
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra("carImage", car.imageResource)
            intent.putExtra("carName", car.name)
            intent.putExtra("carDescription", car.description)
            intent.putExtra("carPrice", car.price)
            intent.putExtra("carCategory", car.category)
            intent.putExtra("carBrand", car.brand)
            intent.putExtra("carReleaseYear", car.releaseYear)
            intent.putExtra("carQuantity", car.quantity)
            return intent
        }
    }
}
