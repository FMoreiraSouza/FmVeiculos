package com.example.fmveiculos.ui.view.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fmveiculos.R
import com.example.fmveiculos.utils.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var textViewName: TextView
    private lateinit var textViewCPF: TextView
    private lateinit var textViewCity: TextView
    private lateinit var textViewState: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        setupViews()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeClientActivity::class.java)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUserEmail = firebaseAuth.currentUser?.email
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("customers").document(currentUserEmail!!)

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
                        textViewName.text = "Nome do Usuário: $name"
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

    private fun setupViews() {
        textViewName = findViewById(R.id.textViewName)
        textViewCPF = findViewById(R.id.textViewCPF)
        textViewCity = findViewById(R.id.textViewCity)
        textViewState = findViewById(R.id.textViewState)
    }
}
