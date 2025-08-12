package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
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
import com.example.fmveiculos.presenter.impl.CarDetailsAdminPresenter
import com.example.fmveiculos.utils.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsAdminActivity : AppCompatActivity(), CarDetailsHomeContract.View {

    private lateinit var presenter: CarDetailsHomeContract.Presenter
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

        Log.d("CarDetailsDebug", "onCreate: Iniciando CarDetailsAdminActivity")

        presenter = CarDetailsAdminPresenter(this, CarRepository())

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
            Log.d("CarDetailsDebug", "onCreate: Clicou na navegação da toolbar")
            navigateToVehicles()
        }

        val carImageResource = intent.getStringExtra("carImage")
        val carName = intent.getStringExtra("carName")
        val carBrand = intent.getStringExtra("carBrand")
        val carQuantity = intent.getIntExtra("carQuantity", 0)
        val carPrice = intent.getDoubleExtra("carPrice", 0.0)
        val carDescription = intent.getStringExtra("carDescription")
        val carCategory = intent.getStringExtra("carCategory")

        Log.d("CarDetailsDebug", "onCreate: Dados recebidos da Intent - carName: $carName, carBrand: $carBrand, carQuantity: $carQuantity, carPrice: $carPrice, carDescription: $carDescription, carCategory: $carCategory")

        Glide.with(this).load(carImageResource).fitCenter().into(carImageView)
        carNameTextView.text = createStyledText("", carName)
        carBrandTextView.text = createStyledText("Marca: ", carBrand)
        carDescriptionTextView.text = createStyledText("Descrição\n", carDescription)
        carQuantityTextView.text = createStyledText("Disponíveis: ", carQuantity.toString())
        carCategoryTextView.text = createStyledText("Categoria: ", carCategory)
        carPriceTextView.text = createStyledText("Preço: R$ ", String.format("%.2f", carPrice))

        btnUpdateFields.setOnClickListener {
            Log.d("CarDetailsDebug", "onCreate: Botão de atualizar campos clicado, chamando showUpdatePopup com carName: $carName")
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
        Log.d("CarDetailsDebug", "showUpdatePopup: Iniciando popup para carName: $carName")
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.popup_update, null)
        val edtAvailable = view.findViewById<EditText>(R.id.edtAvailable)
        val edtPrice = view.findViewById<EditText>(R.id.edtPrice)
        val btnUpdateQuantity = view.findViewById<Button>(R.id.btnUpdateQuantity)
        val btnUpdatePrice = view.findViewById<Button>(R.id.btnUpdatePrice)

        CoroutineScope(Dispatchers.Main).launch {
            Log.d("CarDetailsDebug", "showUpdatePopup: Buscando carro com presenter.getCarByName para carName: $carName")
            presenter.getCarByName(carName)?.let { car ->
                Log.d("CarDetailsDebug", "showUpdatePopup: Carro encontrado - name: ${car.name}, id: ${car.id}, quantity: ${car.quantity}, price: ${car.price}")
                edtAvailable.setText(car.quantity.toString())
                edtPrice.setText(car.price.toString())
            } ?: run {
                Log.d("CarDetailsDebug", "showUpdatePopup: Carro não encontrado para carName: $carName")
                showUpdateError("Carro não encontrado")
            }
        }

        btnUpdateQuantity.setOnClickListener {
            val newQuantity = edtAvailable.text.toString().toIntOrNull()
            Log.d("CarDetailsDebug", "showUpdatePopup: Botão de atualizar quantidade clicado, newQuantity: $newQuantity")
            if (newQuantity != null) {
                Log.d("CarDetailsDebug", "showUpdatePopup: Chamando presenter.updateCarQuantity para carName: $carName, newQuantity: $newQuantity")
                presenter.updateCarQuantity(carName, newQuantity) { updatedQuantity ->
                    carQuantityTextView.text = createStyledText("Disponíveis: ", updatedQuantity.toString())
                    dialog.dismiss()
                }
            } else {
                showUpdateError("Por favor, insira uma quantidade válida")
            }
        }

        btnUpdatePrice.setOnClickListener {
            val newPrice = edtPrice.text.toString().toDoubleOrNull()
            Log.d("CarDetailsDebug", "showUpdatePopup: Botão de atualizar preço clicado, newPrice: $newPrice")
            if (newPrice != null) {
                Log.d("CarDetailsDebug", "showUpdatePopup: Chamando presenter.updateCarPrice para carName: $carName, newPrice: $newPrice")
                presenter.updateCarPrice(carName, newPrice) { updatedPrice ->
                    carPriceTextView.text = createStyledText("Preço: R$ ", String.format("%.2f", updatedPrice))
                    dialog.dismiss()
                }
            } else {
                showUpdateError("Por favor, insira um preço válido")
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun showQuantityUpdated() {
        Log.d("CarDetailsDebug", "showQuantityUpdated: Exibindo mensagem de sucesso para quantidade")
        Toast.makeText(this, "Quantidade atualizada com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun showPriceUpdated() {
        Log.d("CarDetailsDebug", "showPriceUpdated: Exibindo mensagem de sucesso para preço")
        Toast.makeText(this, "Preço atualizado com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun showUpdateError(message: String) {
        Log.d("CarDetailsDebug", "showUpdateError: Erro exibido na UI - message: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToVehicles() {
        Log.d("CarDetailsDebug", "navigateToVehicles: Navegando para VehiclesActivity")
        Navigator().navigateToActivity(this, VehiclesActivity::class.java)
    }
}