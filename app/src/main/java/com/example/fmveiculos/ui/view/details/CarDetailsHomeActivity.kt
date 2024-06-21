package com.example.fmveiculos.ui.view.details

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.example.fmveiculos.model.CarModel
import com.example.fmveiculos.ui.view.vehicles.VehiclesActivity
import com.example.fmveiculos.utils.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CarDetailsHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_home)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, VehiclesActivity::class.java)
        }

        auth = FirebaseAuth.getInstance()

        val carImageResource = intent.getStringExtra("carImage")
        val carName = intent.getStringExtra("carName")
        val carBrand = intent.getStringExtra("carBrand")
        val carQuantity = intent.getIntExtra("carQuantity", 0)
        val carPrice = intent.getDoubleExtra("carPrice", 0.0)
        val carDescription = intent.getStringExtra("carDescription")
        val carCategory = intent.getStringExtra("carCategory")

        val carImageView = findViewById<ImageView>(R.id.carImageView)
        val carNameTextView = findViewById<TextView>(R.id.carNameTextView)
        val carBrandTextView = findViewById<TextView>(R.id.carBrandTextView)
        val carQuantityTextView = findViewById<TextView>(R.id.carQuantityTextView)
        val carPriceTextView = findViewById<TextView>(R.id.carPriceTextView)
        val carDescriptionTextView = findViewById<TextView>(R.id.carDescriptionTextView)
        val carCategoryTextView = findViewById<TextView>(R.id.carCategoryTextView)

        Glide.with(this)
            .load(carImageResource)
            .fitCenter()
            .into(carImageView)

        carNameTextView.text = createStyledText("", carName)
        carBrandTextView.text = createStyledText("Marca: ", carBrand)
        carDescriptionTextView.text = createStyledText("Descrição\n", carDescription)
        carQuantityTextView.text = createStyledText("Disponíveis: ", carQuantity.toString())
        carCategoryTextView.text = createStyledText("Categoria: ", carCategory)
        carPriceTextView.text = createStyledText("Preço: R$ ", String.format("%.2f", carPrice))

        val btnUpdateFields = findViewById<Button>(R.id.btnUpdateFields)
        btnUpdateFields.setOnClickListener {
            showUpdatePopup()
        }
    }

    private fun createStyledText(label: String, value: String?): SpannableString {
        val spannableString = SpannableString("$label$value")
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            label.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun showUpdatePopup() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.popup_update, null)

        val edtAvailable = view.findViewById<EditText>(R.id.edtAvailable)
        val edtPrice = view.findViewById<EditText>(R.id.edtPrice)
        val btnUpdatePrice = view.findViewById<Button>(R.id.btnUpdatePrice)
        val btnUpdateQuantity = view.findViewById<Button>(R.id.btnUpdateQuantity)

        // Obtendo o nome do carro da Intent
        val carName = intent.getStringExtra("carName")

        // Obtendo a quantidade atual do carro do Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("cars")
            .whereEqualTo("name", carName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Assume-se que há apenas um documento com o nome único
                    val documentSnapshot = querySnapshot.documents[0]

                    // Obtendo os dados do documento
                    val car = documentSnapshot.toObject(CarModel::class.java)
                    if (car != null) {
                        val initialQuantity = car.quantity
                        val initialPrice = car.price

                        // Definindo os valores iniciais nos EditTexts dentro do popup
                        edtAvailable.setText(initialQuantity.toString())
                        edtPrice.setText(initialPrice.toString())

                        btnUpdateQuantity.setOnClickListener {
                            val newQuantity = edtAvailable.text.toString().toIntOrNull()

                            if (newQuantity != null) {
                                // Atualizar no Firestore
                                val carRef = db.collection("cars").document(documentSnapshot.id)
                                carRef.update("quantity", newQuantity)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Quantidade atualizada com sucesso!", Toast.LENGTH_SHORT).show()

                                        // Atualiza também na interface principal
                                        findViewById<TextView>(R.id.carQuantityTextView).text = createStyledText("Disponíveis: ", newQuantity.toString())

                                        // Fecha o popup
                                        dialog.dismiss()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Erro ao atualizar quantidade: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Por favor, insira uma quantidade válida.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        btnUpdatePrice.setOnClickListener {
                            val newPrice = edtPrice.text.toString().toDoubleOrNull()

                            if (newPrice != null) {
                                // Atualizar no Firestore
                                val carRef = db.collection("cars").document(documentSnapshot.id)
                                carRef.update("price", newPrice)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Preço atualizado com sucesso!", Toast.LENGTH_SHORT).show()

                                        // Atualiza também na interface principal
                                        findViewById<TextView>(R.id.carPriceTextView).text = createStyledText("Preço: R$ ", String.format("%.2f", newPrice))

                                        // Fecha o popup
                                        dialog.dismiss()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Erro ao atualizar preço: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Por favor, insira um preço válido.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Carro não encontrado.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(this, "Carro não encontrado.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar carro: ${e.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        dialog.setContentView(view)
        dialog.show()
    }





}
