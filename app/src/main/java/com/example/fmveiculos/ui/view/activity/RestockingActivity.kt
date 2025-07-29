package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.CarModel
import com.example.fmveiculos.data.repository.CarRepository
import com.example.fmveiculos.presenter.contract.RestockingContract
import com.example.fmveiculos.presenter.impl.RestockingPresenter
import com.example.fmveiculos.utils.Navigator

class RestockingActivity : AppCompatActivity(), RestockingContract.View {

    private lateinit var presenter: RestockingPresenter
    private lateinit var imageView: ImageView
    private lateinit var noImageText: TextView
    private lateinit var nameField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var priceField: EditText
    private lateinit var brandField: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var yearField: EditText
    private lateinit var quantityField: EditText

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        presenter.setSelectedImage(uri)
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            presenter.setBitmapImage(bitmap)
        }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restocking)

        presenter = RestockingPresenter(this, CarRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        imageView = findViewById(R.id.carImageView)
//        noImageText = findViewById(R.id.noImageText)
        nameField = findViewById(R.id.carNameTextView)
        descriptionField = findViewById(R.id.carDescriptionTextView)
        priceField = findViewById(R.id.carPriceTextView)
        brandField = findViewById(R.id.carBrandTextView)
//        categorySpinner = findViewById(R.id.spinnerCategory)
//        yearField = findViewById(R.id.editTextYear)
//        quantityField = findViewById(R.id.editTextQuantity)

        val categories = arrayOf("Hatch", "Sedan", "SUV", "Picape")
        categorySpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        val selectImageButton = findViewById<Button>(R.id.btnGaleria)
        val takePhotoButton = findViewById<Button>(R.id.btnCamera)
        val saveButton = findViewById<Button>(R.id.btnUpdateFields)

        selectImageButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        takePhotoButton.setOnClickListener {
            takePicture.launch()
        }

        saveButton.setOnClickListener {
            val name = nameField.text.toString()
            val description = descriptionField.text.toString()
            val price = priceField.text.toString().toDoubleOrNull() ?: 0.0
            val brand = brandField.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val year = yearField.text.toString().toIntOrNull() ?: 0
            val quantity = quantityField.text.toString().toIntOrNull() ?: 0

            if (name.isEmpty() || description.isEmpty() || price == 0.0 || brand.isEmpty() || year == 0 || quantity == 0) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val car = CarModel(
                name = name,
                description = description,
                price = price,
                brand = brand,
                category = category,
                releaseYear = year,
                quantity = quantity
            )
            presenter.saveCar(car)
        }
    }

    override fun showImageSelected() {
        noImageText.visibility = android.view.View.GONE
        imageView.visibility = android.view.View.VISIBLE
    }

    override fun showNoImageSelected() {
        noImageText.visibility = android.view.View.VISIBLE
        imageView.visibility = android.view.View.GONE
    }

    override fun showCarSavedSuccess() {
        Toast.makeText(this, "Carro salvo com sucesso!", Toast.LENGTH_SHORT).show()
        navigateToHome()
    }

    override fun showCarSavedError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }
}