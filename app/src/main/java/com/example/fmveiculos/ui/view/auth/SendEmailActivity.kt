package com.example.fmveiculos.ui.view.auth

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.utils.Navigator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SendEmailActivity : AppCompatActivity() {

    private lateinit var emailField: TextInputEditText
    private lateinit var sendEmailButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)
        setupViews()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, LoginActivity::class.java)
        }

        firebaseAuth = FirebaseAuth.getInstance()

        sendEmailButton.setOnClickListener {
            val email = emailField.text.toString().trim()

            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Por favor, insira um email válido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViews() {
        emailField = findViewById(R.id.emailField)
        sendEmailButton = findViewById(R.id.sendEmailButton)
    }

    private fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Um email de recuperação foi enviado para $email", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Falha ao enviar email de recuperação: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
