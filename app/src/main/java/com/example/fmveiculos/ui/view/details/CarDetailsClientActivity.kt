package com.example.fmveiculos.ui.view.details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
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
import com.example.fmveiculos.ui.view.home.HomeClientActivity
import com.example.fmveiculos.utils.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CarDetailsClientActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_client)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeClientActivity::class.java)
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
        val whatsappLayout = findViewById<LinearLayout>(R.id.whatsappLayout)
        val interestButton = findViewById<Button>(R.id.interestButton)

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

        val clickAnimation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.button_highlight)

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
            openWhatsApp()
        }

        interestButton.setOnClickListener {
            it.startAnimation(clickAnimation)
            confirmInterest()
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

    fun openWhatsApp() {
        val phoneNumber = "+5588992696529"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$phoneNumber")
        startActivity(intent)
    }

    fun confirmInterest() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userDocRef = db.collection("userInfo").document(userId)

            userDocRef.get()
                .addOnSuccessListener { userDocument ->
                    if (userDocument != null && userDocument.contains("name")) {
                        val name = userDocument.getString("name")
                        Log.d("MainActivity", "Name: $name")

                        val carName = intent.getStringExtra("carName")
                        val carPrice = intent.getDoubleExtra("carPrice", 0.0)
                        val timestamp = Calendar.getInstance().time.toString()
                        val status = "Pendente"

                        // Verificar se a quantidade do carro é maior que 0
                        val carQuantity = intent.getIntExtra("carQuantity", 0)
                        if (carQuantity > 0) {
                            // Processar o interesse apenas se a quantidade for maior que 0
                            db.collection("cars").whereEqualTo("name", carName)
                                .get()
                                .addOnSuccessListener { carDocuments ->
                                    if (carDocuments != null && !carDocuments.isEmpty) {
                                        val carDocument = carDocuments.documents[0]
                                        val carId = carDocument.id

                                        val interestData = hashMapOf(
                                            "carId" to carId,
                                            "userId" to userId,
                                            "name" to name,
                                            "carName" to carName,
                                            "carPrice" to carPrice,
                                            "timestamp" to timestamp,
                                            "status" to status
                                        )

                                        db.collection("interests")
                                            .add(interestData)
                                            .addOnSuccessListener { documentReference ->
                                                val documentId = documentReference.id
                                                documentReference.update("id", documentId)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            this,
                                                            "Interesse de pedido salvo com sucesso",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Toast.makeText(
                                                            this,
                                                            "Erro ao salvar o ID do documento",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(
                                                    this,
                                                    "Erro na solicitação de pedido",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    } else {
                                        Log.d("MainActivity", "Carro não encontrado")
                                        Toast.makeText(
                                            this,
                                            "Carro não encontrado",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("MainActivity", "Erro ao obter carro: ", exception)
                                    Toast.makeText(
                                        this,
                                        "Erro ao obter carro",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        } else {
                            // Exibir o Toast de modelo indisponível se a quantidade for 0
                            Toast.makeText(
                                this,
                                "Este modelo está indisponível no momento",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Log.d("MainActivity", "Campo 'name' não encontrado no documento do usuário")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("MainActivity", "Erro ao obter documento do usuário: ", exception)
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_LONG).show()
        }
    }




}
