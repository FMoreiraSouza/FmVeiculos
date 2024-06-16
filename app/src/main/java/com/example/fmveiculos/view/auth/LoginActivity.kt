package com.example.fmveiculos.view.auth

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView
    private lateinit var dealershipName: TextView
    private lateinit var loginClient: TextView
    private lateinit var viewModel: LoginViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var email: String
        var password: String

        firebaseAuth = FirebaseAuth.getInstance()



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

    override fun onStart() {
        super.onStart()
        val cameFromRegister = intent.getBooleanExtra("CAME_FROM_REGISTER", false)
        if (!cameFromRegister) {
            val isFirstLogin = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getBoolean("is_first_login", true)
            if (!isFirstLogin) {
                viewModel.checkLoggedUser()
            }
        }
    }


    private fun observeViewModel() {
        val loginObserver = Observer<Boolean> { success ->
            if (success) {
                setFirstLoginFlag(false)
                redirectToHome()
                Toast.makeText(this, "Login bem sucedido!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.loginResult.observe(this, loginObserver)

        val loggedUserObserver = Observer<Boolean>{success->
            if(success){
                redirectToHome()
            }
        }
        viewModel.navigateToHome.observe(this, loggedUserObserver)
    }

    private fun redirectToHome() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val email = user.email
            if (email != null && email.endsWith("@fmveiculos.com")) {
                Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
            } else {
                Navigator().navigateToActivity(this, HomeClientActivity::class.java)
            }
        }
    }

    private fun setFirstLoginFlag(isFirstLogin: Boolean) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_first_login", isFirstLogin)
            apply()
        }
    }
}
