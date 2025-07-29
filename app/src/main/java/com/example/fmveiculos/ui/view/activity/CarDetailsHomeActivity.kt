package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.presenter.contract.CarDetailsHomeContract
import com.example.fmveiculos.presenter.impl.CarDetailsHomePresenter
import com.example.fmveiculos.utils.Navigator
import android.graphics.Typeface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsHomeActivity : AppCompatActivity(), CarDetailsHomeContract.View {

    private lateinit var presenter: CarDetailsHomePresenter
    private lateinit var toolbar: Toolbar
    private lateinit var carImageView: ImageView
    private lateinit var carNameTextView: TextView
    private lateinit var carBrandTextView: TextView
    private lateinit var carQuantityTextView: TextView
    private lateinit var carPriceTextView: TextView
    private lateinit var carDescriptionTextView: TextView
    private lateinit var carCategoryTextView: TextView
    private lateinit var btnUpdateFields: Button

    @SuppressLint("MissingInflatedId", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_home)

        presenter = CarDetailsHomePresenter(this, CarRepository())

        toolbar = findViewById(R.id.toolbar)
        carImageView = findViewById(R.id.carImageView)
        carNameTextView = findViewById(R.id.carNameTextView)
        carBrandTextView = findViewById(R.id.carBrandTextView)
        carQuantityTextView = findViewById(R.id.carQuantityTextView)
        carPriceTextView = findViewById(R.id.carPriceTextView)
        carDescriptionTextView = findViewById(R.id.carDescriptionTextView)
        carCategoryTextView = findViewById(R.id.carCategoryTextView)
        btnUpdateFields = findViewById(R.id.btnUpdateFields)

        toolbar.setNavigationOnClickListener {
            navigateToVehicles()
        }

        val carImageResource = intent.getStringExtra("carImage")
        val carName = intent.getStringExtra("carName")
        val carBrand = intent.getStringExtra("carBrand")
        val carQuantity = intent.getIntExtra("carQuantity", 0)
        val carPrice = intent.getDoubleExtra("carPrice", 0.0)
        val carDescription = intent.getStringExtra("carDescription")
        val carCategory = intent.getStringExtra("carCategory")

        Glide.with(this).load(carImageResource).fitCenter().into(carImageView)
        carNameTextView.text = createStyledText("", carName)
        carBrandTextView.text = createStyledText("Marca: ", carBrand)
        carDescriptionTextView.text = createStyledText("Descrição\n", carDescription)
        carQuantityTextView.text = createStyledText("Disponíveis: ", carQuantity.toString())
        carCategoryTextView.text = createStyledText("Categoria: ", carCategory)
        carPriceTextView.text = createStyledText("Preço: R$ ", String.format("%.2f", carPrice))

        btnUpdateFields.setOnClickListener {
            showUpdatePopup(carName ?: "")
        }
    }

    private fun createStyledText(label: String, value: String?): SpannableString {
        val spannableString = SpannableString("$label$value")
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    @SuppressLint("InflateParams")
    private fun showUpdatePopup(carName: String) {
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.popup_update, null)
        val edtAvailable = view.findViewById<EditText>(R.id.edtAvailable)
        val edtPrice = view.findViewById<EditText>(R.id.edtPrice)
        val btnUpdateQuantity = view.findViewById<Button>(R.id.btnUpdateQuantity)
        val btnUpdatePrice = view.findViewById<Button>(R.id.btnUpdatePrice)

        CoroutineScope(Dispatchers.Main).launch {
            presenter.getCarByName(carName)?.let { car ->
                edtAvailable.setText(car.quantity.toString())
                edtPrice.setText(car.price.toString())
            }
        }

        btnUpdateQuantity.setOnClickListener {
            val newQuantity = edtAvailable.text.toString().toIntOrNull()
            if (newQuantity != null) {
                presenter.updateCarQuantity(carName, newQuantity)
            } else {
                showUpdateError("Por favor, insira uma quantidade válida")
            }
        }

        btnUpdatePrice.setOnClickListener {
            val newPrice = edtPrice.text.toString().toDoubleOrNull()
            if (newPrice != null) {
                presenter.updateCarPrice(carName, newPrice)
            } else {
                showUpdateError("Por favor, insira um preço válido")
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun showQuantityUpdated() {
        Toast.makeText(this, "Quantidade atualizada com sucesso!", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.Main).launch {
            presenter.getCarByName(carNameTextView.text.toString())?.let {
                carQuantityTextView.text = createStyledText("Disponíveis: ", it.quantity.toString())
            }
        }
    }

    @SuppressLint("DefaultLocale")
    override fun showPriceUpdated() {
        Toast.makeText(this, "Preço atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.Main).launch {
            presenter.getCarByName(carNameTextView.text.toString())?.let {
                carPriceTextView.text = createStyledText("Preço: R$ ", String.format("%.2f", it.price))
            }
        }
    }

    override fun showUpdateError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToVehicles() {
        Navigator().navigateToActivity(this, VehiclesActivity::class.java)
    }
}