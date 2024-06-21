package com.example.fmveiculos.ui.view.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fmveiculos.R
import com.example.fmveiculos.utils.Navigator
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.ui.view.home.HomeClientActivity
import com.example.fmveiculos.viewModel.auth.LoginViewModel
import com.example.fmveiculos.utils.setupUI
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordTextButton: TextView
    private lateinit var registerButton: TextView
    private lateinit var dealershipName: TextView
    private lateinit var viewModel: LoginViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        forgotPasswordTextButton = findViewById(R.id.forgotPasswordTextButton)
        registerButton = findViewById(R.id.registerButton)
        dealershipName = findViewById(R.id.logoTextDealershipName)

        val emailErrorText = findViewById<TextView>(R.id.emailErrorText)
        val passwordErrorText = findViewById<TextView>(R.id.passwordErrorText)

        setupUI(dealershipName)

        loginButton.setOnClickListener {
            hideKeyboard()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty()) {
                emailErrorText.visibility = View.VISIBLE
                emailErrorText.text = "Email vazio"
            } else {
                emailErrorText.visibility = View.GONE
            }

            if (password.isEmpty()) {
                passwordErrorText.visibility = View.VISIBLE
                passwordErrorText.text = "Senha vazia"
            } else {
                passwordErrorText.visibility = View.GONE
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(email, password)
            }
        }

        val clickAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_highlight)

        clickAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                forgotPasswordTextButton.clearAnimation()
                registerButton.clearAnimation()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        forgotPasswordTextButton.setOnClickListener {
            it.startAnimation(clickAnimation)
            Navigator().navigateToActivity(this, SendEmailActivity::class.java)
        }

        registerButton.setOnClickListener {
            it.startAnimation(clickAnimation)
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
                Toast.makeText(this, "Login bem sucedido!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Email ou Senha inválidos!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loginResult.observe(this, loginObserver)

        val loggedUserObserver = Observer<Boolean>{ success ->
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

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
