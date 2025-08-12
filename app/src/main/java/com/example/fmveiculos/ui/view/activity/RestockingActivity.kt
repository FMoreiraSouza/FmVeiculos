package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
        Log.d("RestockingDebug", "pickImage: URI retornado - uri: ${uri?.toString() ?: "null"}")
        presenter.setSelectedImage(uri)
        if (uri != null) {
            imageView.setImageURI(uri)
            Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("RestockingDebug", "pickImage: Nenhuma imagem selecionada, restaurando imagem padrão")
            presenter.restoreDefaultImage()
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        Log.d("RestockingDebug", "takePicture: Bitmap retornado - bitmap: ${if (bitmap != null) "presente" else "null"}")
        presenter.setBitmapImage(bitmap)
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
            Toast.makeText(this, "Foto tirada", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("RestockingDebug", "takePicture: Nenhuma foto tirada, restaurando imagem padrão")
            presenter.restoreDefaultImage()
            Toast.makeText(this, "Nenhuma foto tirada", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restocking)

        Log.d("RestockingDebug", "onCreate: Iniciando RestockingActivity")

        presenter = RestockingPresenter(this, CarRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            Log.d("RestockingDebug", "onCreate: Clicou na navegação da toolbar")
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

        Log.d("RestockingDebug", "onCreate: Inicializando ImageView com imagem padrão")
        presenter.restoreDefaultImage()

        val selectImageButton = findViewById<ImageButton>(R.id.btnGaleria)
        val takePhotoButton = findViewById<ImageButton>(R.id.btnCamera)
        val saveButton = findViewById<Button>(R.id.btnFinalizarReposicao)

        selectImageButton.setOnClickListener {
            Log.d("RestockingDebug", "onCreate: Botão de galeria clicado, iniciando pickImage")
            pickImage.launch("image/*")
        }

        takePhotoButton.setOnClickListener {
            Log.d("RestockingDebug", "onCreate: Botão de câmera clicado, iniciando takePicture")
            takePicture.launch()
        }

        saveButton.setOnClickListener {
            Log.d("RestockingDebug", "onCreate: Botão de salvar clicado")
            val name = nameField.text.toString()
            val description = descriptionField.text.toString()
            val price = priceField.text.toString().toDoubleOrNull() ?: 0.0
            val brand = brandField.text.toString()
            val category = categoryField.text.toString()
            val year = yearField.text.toString().toIntOrNull() ?: 0
            val quantity = quantityField.text.toString().toIntOrNull() ?: 0

            Log.d("RestockingDebug", "onCreate: Dados do formulário - name: $name, description: $description, price: $price, brand: $brand, category: $category, year: $year, quantity: $quantity")

            if (name.isEmpty() || description.isEmpty() || price == 0.0 || brand.isEmpty() || category.isEmpty() || year == 0 || quantity == 0) {
                Log.d("RestockingDebug", "onCreate: Campos inválidos, exibindo erro")
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
            Log.d("RestockingDebug", "onCreate: Chamando presenter.saveCar para carro: ${car.name}")
            presenter.saveCar(car)
        }
    }

    override fun showImageSelected() {
        Log.d("RestockingDebug", "showImageSelected: Definindo ImageView como visível")
        imageView.visibility = android.view.View.VISIBLE
    }

    override fun showNoImageSelected() {
        Log.d("RestockingDebug", "showNoImageSelected: Restaurando imagem padrão")
        imageView.setImageResource(R.drawable.galeria)
        imageView.visibility = android.view.View.VISIBLE
    }

    override fun showCarSavedSuccess() {
        Log.d("RestockingDebug", "showCarSavedSuccess: Carro salvo com sucesso")
        Toast.makeText(this, "Carro salvo com sucesso!", Toast.LENGTH_SHORT).show()
        navigateToHome()
    }

    override fun showCarSavedError(message: String) {
        Log.d("RestockingDebug", "showCarSavedError: Erro ao salvar carro - message: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        Log.d("RestockingDebug", "navigateToHome: Navegando para HomeAdminActivity")
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }
}