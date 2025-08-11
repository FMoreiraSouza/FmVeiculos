package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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
    private lateinit var nameField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var priceField: EditText
    private lateinit var brandField: EditText
    private lateinit var categoryField: EditText
    private lateinit var yearField: EditText
    private lateinit var quantityField: EditText

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        presenter.setSelectedImage(uri)
        if (uri != null) {
            imageView.setImageURI(uri)
            Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        presenter.setBitmapImage(bitmap)
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
            Toast.makeText(this, "Foto tirada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Nenhuma foto tirada", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restocking)

        presenter = RestockingPresenter(this, CarRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        imageView = findViewById(R.id.imageViewProduto)
        nameField = findViewById(R.id.editTextNome)
        descriptionField = findViewById(R.id.editTextDescricao)
        priceField = findViewById(R.id.editTextPreco)
        brandField = findViewById(R.id.editTextMarca)
        categoryField = findViewById(R.id.editTextCategoria)
        yearField = findViewById(R.id.editTextAnoLancamento)
        quantityField = findViewById(R.id.editTextQuantidade)

        val selectImageButton = findViewById<ImageButton>(R.id.btnGaleria)
        val takePhotoButton = findViewById<ImageButton>(R.id.btnCamera)
        val saveButton = findViewById<Button>(R.id.btnFinalizarReposicao)

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
            val category = categoryField.text.toString()
            val year = yearField.text.toString().toIntOrNull() ?: 0
            val quantity = quantityField.text.toString().toIntOrNull() ?: 0

            if (name.isEmpty() || description.isEmpty() || price == 0.0 || brand.isEmpty() || category.isEmpty() || year == 0 || quantity == 0) {
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
        imageView.visibility = android.view.View.VISIBLE
    }

    override fun showNoImageSelected() {
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