package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.CarDetailsClientContract
import com.example.fmveiculos.presenter.impl.CarDetailsClientPresenter
import com.example.fmveiculos.utils.Navigator
import androidx.core.net.toUri

class CarDetailsClientActivity : AppCompatActivity(), CarDetailsClientContract.View {

    private lateinit var presenter: CarDetailsClientPresenter
    private lateinit var carNameTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_client)

        presenter = CarDetailsClientPresenter(this, AuthRepository(), InterestRepository(), CarRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        val carImageResource = intent.getStringExtra("carImage")
        val carName = intent.getStringExtra("carName")
        val carBrand = intent.getStringExtra("carBrand")
        val carQuantity = intent.getIntExtra("carQuantity", 0)
        val carPrice = intent.getDoubleExtra("carPrice", 0.0)
        val carDescription = intent.getStringExtra("carDescription")
        val carCategory = intent.getStringExtra("carCategory")

        val carImageView: ImageView = findViewById(R.id.carImageView)
        carNameTextView = findViewById(R.id.carNameTextView)
        val carBrandTextView: TextView = findViewById(R.id.carBrandTextView)
        val carQuantityTextView: TextView = findViewById(R.id.carQuantityTextView)
        val carPriceTextView: TextView = findViewById(R.id.carPriceTextView)
        val carDescriptionTextView: TextView = findViewById(R.id.carDescriptionTextView)
        val carCategoryTextView: TextView = findViewById(R.id.carCategoryTextView)

        Glide.with(this).load(carImageResource).fitCenter().into(carImageView)
        carNameTextView.text = createStyledText("", carName)
        carBrandTextView.text = createStyledText("Marca: ", carBrand)
        carDescriptionTextView.text = createStyledText("Descrição\n", carDescription)
        carQuantityTextView.text = createStyledText("Disponíveis: ", carQuantity.toString())
        carCategoryTextView.text = createStyledText("Categoria: ", carCategory)
        carPriceTextView.text = createStyledText("Preço: R$ ", String.format("%.2f", carPrice))

        val btnConfirmInterest: Button = findViewById(R.id.buttonConfirmInterest)
        val btnWhatsApp: Button = findViewById(R.id.sendEmailButton)

        btnConfirmInterest.setOnClickListener {
            presenter.confirmInterest(
                userId = AuthRepository().getCurrentUserEmail(),
                carName = carName ?: "",
                carPrice = carPrice,
                carQuantity = carQuantity
            )
        }

        btnWhatsApp.setOnClickListener {
            presenter.onWhatsAppClicked()
        }
    }

    private fun createStyledText(label: String, value: String?): SpannableString {
        val spannableString = SpannableString("$label$value")
        spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    override fun showInterestSaved() {
        Toast.makeText(this, "Interesse registrado com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun showInterestError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun openWhatsApp(phoneNumber: String) {
        val uri = "https://api.whatsapp.com/send?phone=$phoneNumber".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeClientActivity::class.java)
    }
}