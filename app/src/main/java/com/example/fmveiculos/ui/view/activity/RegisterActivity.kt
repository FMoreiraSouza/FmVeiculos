package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.presenter.contract.RegisterContract
import com.example.fmveiculos.presenter.impl.RegisterPresenter
import com.example.fmveiculos.utils.Masks
import com.example.fmveiculos.utils.Navigator

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var presenter: RegisterPresenter
    private lateinit var finishRegisterButton: Button
    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var cpfField: EditText
    private lateinit var cityField: EditText
    private lateinit var stateField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this, AuthRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            navigateToLogin()
        }

        finishRegisterButton = findViewById(R.id.buttonFinishRegister)
        nameField = findViewById(R.id.editTextNome)
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextSenha)
        cpfField = findViewById(R.id.editTextCPF)
        cityField = findViewById(R.id.editTextCity)
        stateField = findViewById(R.id.editTextState)

        cpfField.addTextChangedListener(Masks(cpfField))

        finishRegisterButton.setOnClickListener {
            hideKeyboard()
            presenter.register(
                emailField.text.toString(),
                passwordField.text.toString(),
                cpfField.text.toString(),
                nameField.text.toString(),
                cityField.text.toString(),
                stateField.text.toString()
            )
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess() {
        Toast.makeText(this, "Cadastro bem sucedido", Toast.LENGTH_LONG).show()
        setFirstLoginFlag(true)
    }

    override fun navigateToLogin() {
        val extras = Bundle().apply {
            putBoolean("CAME_FROM_REGISTER", true)
        }
        Navigator().navigateToActivity(this, LoginActivity::class.java, extras)
        finish()
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