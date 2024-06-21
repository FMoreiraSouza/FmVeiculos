package com.example.fmveiculos.ui.view.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var textViewName: TextView
    private lateinit var textViewCPF: TextView
    private lateinit var textViewCity: TextView
    private lateinit var textViewState: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var originActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupViews()

        originActivity = intent.getStringExtra("originActivity")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            handleNavigationClick()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val db = FirebaseFirestore.getInstance()
        val docRef = firebaseAuth.currentUser?.let { db.collection("userInfo").document(it.uid) }

        if (docRef != null) {
            docRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            val name = document.getString("name")
                            val cpf = document.getString("cpf")
                            val city = document.getString("city")
                            val state = document.getString("state")

                            // Exibir os dados nos TextViews
                            textViewName.text = "Usuário: $name"
                            textViewCPF.text = "CPF: $cpf"
                            textViewCity.text = "Cidade: $city"
                            textViewState.text = "Estado: $state"
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.exception)
                    }
                }
        }
    }

    private fun setupViews() {
        textViewName = findViewById(R.id.textViewName)
        textViewCPF = findViewById(R.id.textViewCPF)
        textViewCity = findViewById(R.id.textViewCity)
        textViewState = findViewById(R.id.textViewState)
    }

    private fun handleNavigationClick() {
        when (originActivity) {
            HomeClientActivity::class.java.name -> {
                finish()
            }
            HomeAdminActivity::class.java.name -> {
                finish()
            }
            else -> {
                Log.e(TAG, "Tela de origem desconhecida: $originActivity")
                finish()
            }
        }
    }
}
