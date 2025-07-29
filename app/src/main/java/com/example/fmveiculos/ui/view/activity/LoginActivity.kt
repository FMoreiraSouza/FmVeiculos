package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.presenter.contract.LoginContract
import com.example.fmveiculos.presenter.impl.LoginPresenter
import com.example.fmveiculos.utils.Navigator
import com.example.fmveiculos.utils.setupUI

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordTextButton: TextView
    private lateinit var registerButton: TextView
    private lateinit var dealershipName: TextView
    private lateinit var emailErrorText: TextView
    private lateinit var passwordErrorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this, AuthRepository())

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        forgotPasswordTextButton = findViewById(R.id.forgotPasswordTextButton)
        registerButton = findViewById(R.id.registerButton)
        dealershipName = findViewById(R.id.logoTextDealershipName)
        emailErrorText = findViewById(R.id.emailErrorText)
        passwordErrorText = findViewById(R.id.passwordErrorText)

        setupUI(dealershipName)

        loginButton.setOnClickListener {
            hideKeyboard()
            presenter.login(emailField.text.toString(), passwordField.text.toString())
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
            presenter.onForgotPasswordClicked()
        }

        registerButton.setOnClickListener {
            it.startAnimation(clickAnimation)
            presenter.onRegisterClicked()
        }
    }

    override fun onStart() {
        super.onStart()
        val cameFromRegister = intent.getBooleanExtra("CAME_FROM_REGISTER", false)
        if (!cameFromRegister) {
            val isFirstLogin = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getBoolean("is_first_login", true)
            if (!isFirstLogin) {
                presenter.checkLoggedUser()
            }
        }
    }

    override fun showEmailError(message: String) {
        emailErrorText.visibility = android.view.View.VISIBLE
        emailErrorText.text = message
    }

    override fun clearEmailError() {
        emailErrorText.visibility = android.view.View.GONE
    }

    override fun showPasswordError(message: String) {
        passwordErrorText.visibility = android.view.View.VISIBLE
        passwordErrorText.text = message
    }

    override fun clearPasswordError() {
        passwordErrorText.visibility = android.view.View.GONE
    }

    override fun showLoginSuccess() {
        setFirstLoginFlag(false)
        Toast.makeText(this, "Login bem sucedido!", Toast.LENGTH_SHORT).show()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome(isAdmin: Boolean) {
        val destination = if (isAdmin) HomeAdminActivity::class.java else HomeClientActivity::class.java
        Navigator().navigateToActivity(this, destination)
    }

    override fun navigateToForgotPassword() {
        Navigator().navigateToActivity(this, SendEmailActivity::class.java)
    }

    override fun navigateToRegister() {
        Navigator().navigateToActivity(this, RegisterActivity::class.java)
    }

    @SuppressLint("UseKtx")
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