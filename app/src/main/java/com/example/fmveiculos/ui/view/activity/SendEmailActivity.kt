package com.example.fmveiculos.ui.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.utils.Navigator
import com.google.android.material.textfield.TextInputEditText

class SendEmailActivity : AppCompatActivity() {

    private lateinit var emailField: TextInputEditText
    private lateinit var sendEmailButton: Button
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, LoginActivity::class.java)
        }

        emailField = findViewById(R.id.emailField)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        sendEmailButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isNotEmpty()) {
                if (authRepository.sendPasswordResetEmail(email)) {
                    Toast.makeText(this, "Um email de recuperação foi enviado para $email", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Falha ao enviar email de recuperação", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Por favor, insira um email válido.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}