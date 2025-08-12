package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.utils.Navigator
import com.google.firebase.auth.FirebaseAuth
import androidx.core.net.toUri
import com.example.fmveiculos.presenter.contract.CarDetailsClientContract
import com.example.fmveiculos.presenter.impl.CarDetailsClientPresenter

class CarDetailsClientActivity : AppCompatActivity(), CarDetailsClientContract.View {

    private lateinit var presenter: CarDetailsClientPresenter
    private lateinit var carImageView: ImageView
    private lateinit var carNameTextView: TextView
    private lateinit var carBrandTextView: TextView
    private lateinit var carQuantityTextView: TextView
    private lateinit var carPriceTextView: TextView
    private lateinit var carDescriptionTextView: TextView
    private lateinit var carCategoryTextView: TextView
    private lateinit var whatsappLayout: LinearLayout
    private lateinit var interestButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_client)

        presenter = CarDetailsClientPresenter(this, AuthRepository(), CarRepository(), InterestRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            presenter.onBackClicked()
        }

        carImageView = findViewById(R.id.carImageView)
        carNameTextView = findViewById(R.id.carNameTextView)
        carBrandTextView = findViewById(R.id.carBrandTextView)
        carQuantityTextView = findViewById(R.id.carQuantityTextView)
        carPriceTextView = findViewById(R.id.carPriceTextView)
        carDescriptionTextView = findViewById(R.id.carDescriptionTextView)
        carCategoryTextView = findViewById(R.id.carCategoryTextView)
        whatsappLayout = findViewById(R.id.whatsappLayout)
        interestButton = findViewById(R.id.buttonConfirmInterest)

        val clickAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_highlight)
        clickAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                whatsappLayout.clearAnimation()
                interestButton.clearAnimation()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        whatsappLayout.setOnClickListener {
            it.startAnimation(clickAnimation)
            presenter.onWhatsAppClicked()
        }

        interestButton.setOnClickListener {
            it.startAnimation(clickAnimation)
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val carName = intent.getStringExtra("carName")
            val carPrice = intent.getDoubleExtra("carPrice", 0.0)
            presenter.confirmInterest(userId, carName, carPrice)
        }

        presenter.loadCarDetails(
            carName = intent.getStringExtra("carName"),
            carBrand = intent.getStringExtra("carBrand"),
            carQuantity = intent.getIntExtra("carQuantity", 0),
            carPrice = intent.getDoubleExtra("carPrice", 0.0),
            carDescription = intent.getStringExtra("carDescription"),
            carCategory = intent.getStringExtra("carCategory"),
            carReleaseYear = intent.getIntExtra("carReleaseYear", 0),
            carImageResource = intent.getStringExtra("carImage")
        )
    }

    override fun displayCarDetails(
        carName: String?,
        carBrand: String?,
        carQuantity: String,
        carPrice: String,
        carDescription: String?,
        carCategory: String?,
        carReleaseYear: String,
        carImageResource: String?
    ) {
        carNameTextView.text = createStyledText("", carName ?: "N/A")
        carBrandTextView.text = createStyledText("Marca: ", carBrand ?: "N/A")
        carQuantityTextView.text = createStyledText("Disponíveis: ", carQuantity)
        carPriceTextView.text = createStyledText("Preço: ", carPrice)
        carDescriptionTextView.text = createStyledText("Descrição\n", carDescription ?: "N/A")
        carCategoryTextView.text = createStyledText("Categoria: ", carCategory ?: "N/A")
        Glide.with(this)
            .load(carImageResource)
            .fitCenter()
            .into(carImageView)
    }

    override fun showInterestSuccess() {
        Toast.makeText(this, "Interesse de pedido salvo com sucesso", Toast.LENGTH_LONG).show()
    }

    override fun showInterestError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showWhatsAppError() {
        Toast.makeText(this, "Erro ao abrir o WhatsApp", Toast.LENGTH_LONG).show()
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeClientActivity::class.java)
    }

    override fun openWhatsApp(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://wa.me/$phoneNumber".toUri()
            startActivity(intent)
        } catch (_: Exception) {
            showWhatsAppError()
        }
    }

    private fun createStyledText(label: String, value: String): SpannableString {
        val spannableString = SpannableString("$label$value")
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            label.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}