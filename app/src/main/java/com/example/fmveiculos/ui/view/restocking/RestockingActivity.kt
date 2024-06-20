package com.example.fmveiculos.ui.view.restocking

import CarModel
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.fmveiculos.R
import com.example.fmveiculos.utils.Navigator
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.viewModel.restocking.RestockingViewModel
import com.google.android.material.navigation.NavigationView

class RestockingActivity : AppCompatActivity() {

    private lateinit var restockingViewModel: RestockingViewModel
    private lateinit var image: ImageView

    private val abrirGaleria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            image.setImageURI(uri)
            restockingViewModel.setSelectedImage(uri)
            Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_LONG).show()
        }
    }

    private val abrirCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultadoActivity ->
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultadoActivity.data?.extras?.getParcelable("data")
        } else {
            resultadoActivity.data?.extras?.get("data") as Bitmap?
        }
        bitmap?.let {
            image.setImageBitmap(it)
            restockingViewModel.setBitmapImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restocking)

        restockingViewModel = ViewModelProvider(this).get(RestockingViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
        }

        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextDescricao = findViewById<EditText>(R.id.editTextDescricao)
        val editTextPreco = findViewById<EditText>(R.id.editTextPreco)
        val editTextMarca = findViewById<EditText>(R.id.editTextMarca)
        val editTextCategoria = findViewById<EditText>(R.id.editTextCategoria)
        val editTextAnoLancamento = findViewById<EditText>(R.id.editTextAnoLancamento)
        val editTextQuantidade = findViewById<EditText>(R.id.editTextQuantidade)

        image = findViewById(R.id.imageViewProduto)
        val gallery = findViewById<ImageButton>(R.id.btnGaleria)
        val camera = findViewById<ImageButton>(R.id.btnCamera)
        val finalizar = findViewById<Button>(R.id.btnFinalizarReposicao)

        gallery.setOnClickListener {
            abrirGaleria.launch("image/*")
        }

        camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            abrirCamera.launch(intent)
        }

        finalizar.setOnClickListener {
            val car = CarModel(
                name = editTextNome.text.toString(),
                description = editTextDescricao.text.toString(),
                price = editTextPreco.text.toString().toDoubleOrNull() ?: 0.0,
                brand = editTextMarca.text.toString(),
                releaseYear = editTextAnoLancamento.text.toString().toIntOrNull() ?: 0,
                quantity = editTextQuantidade.text.toString().toIntOrNull() ?: 0,
                category = editTextCategoria.text.toString(),
                imageResource = null.toString()
            )

            if (restockingViewModel.uriSelectedImage != null || restockingViewModel.bitmapSelectedImage != null) {
                restockingViewModel.uploadImageAndSaveCar(car,
                    onSuccess = {
                        Toast.makeText(this, "Carro salvo com sucesso", Toast.LENGTH_LONG).show()
                        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
            } else {
                Toast.makeText(this, "Por favor, selecione uma imagem", Toast.LENGTH_LONG).show()
            }
        }
    }
}
