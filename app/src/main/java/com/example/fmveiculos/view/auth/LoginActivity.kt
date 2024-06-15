package com.example.fmveiculos.view.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fmveiculos.R
import com.example.fmveiculos.utils.Navigator
import com.example.fmveiculos.view.home.HomeAdminActivity
import com.example.fmveiculos.view.home.HomeClientActivity
import com.example.fmveiculos.viewModel.auth.LoginViewModel
import com.example.fmveiculos.utils.setupUI

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView
    private lateinit var dealershipName: TextView
    private lateinit var loginClient: TextView

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var email: String
        var password: String

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        dealershipName = findViewById(R.id.logoTextDealershipName)
        loginClient = findViewById(R.id.logoTextClient)

        setupUI(dealershipName)

        loginButton.setOnClickListener {
            email = emailField.text.toString()
            password = passwordField.text.toString()
            viewModel.loginUser(email, password)
        }

        registerButton.setOnClickListener {
            Navigator().navigateToActivity(this, RegisterActivity::class.java)
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        val loginObserver = Observer<Boolean> { success ->
            if (success) {
                val email = emailField.text.toString()
                if (email.endsWith("@fmveiculos.com")) {
                    Toast.makeText(this, "Login bem sucedido", Toast.LENGTH_LONG).show()
                    Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
                } else {
                    Toast.makeText(this, "Login bem sucedido como cliente", Toast.LENGTH_LONG)
                        .show()
                    Navigator().navigateToActivity(this, HomeClientActivity::class.java)
                }
            } else {
                Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.loginResult.observe(this, loginObserver)
    }

}
