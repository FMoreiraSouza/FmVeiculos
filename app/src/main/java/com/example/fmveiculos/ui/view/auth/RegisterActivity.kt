package com.example.fmveiculos.ui.view.auth

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fmveiculos.R
import com.example.fmveiculos.model.UserModel
import com.example.fmveiculos.utils.Navigator
import com.example.fmveiculos.viewModel.auth.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var finishRegisterButton: Button
    private lateinit var viewModel: RegisterViewModel
    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var cpfField: EditText
    private lateinit var cepField: EditText
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(findViewById(R.id.toolbar))
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        finishRegisterButton = findViewById(R.id.buttonFinishRegister)
        nameField = findViewById(R.id.editTextNome)
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextSenha)
        cpfField = findViewById(R.id.editTextCPF)
        cepField = findViewById(R.id.editTextCEP)

        finishRegisterButton.setOnClickListener {
            user = UserModel(
                name = nameField.text.toString(),
                email = emailField.text.toString(),
                password = passwordField.text.toString(),
                cpf = cpfField.text.toString(),
                cep = cepField.text.toString(),
            )
            viewModel.registerUser(user.email, user.password)
        }
        observeViewModel()

    }
    private fun observeViewModel() {
        val registerObserver = Observer<Boolean> { success ->
            if (success) {
                Toast.makeText(this, "Cadastro bem sucedido", Toast.LENGTH_LONG).show()
                setFirstLoginFlag(true)
                val extras = Bundle().apply {
                    putBoolean("CAME_FROM_REGISTER", true)
                }
                Navigator().navigateToActivity(this, LoginActivity::class.java, extras)
                finish()

            } else {
                Toast.makeText(
                    this,
                    "Clientes não podem ser cadastrados no domínio fmveiculos.com",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.registerResult.observe(this, registerObserver)
    }

    private fun setFirstLoginFlag(isFirstLogin: Boolean) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_first_login", isFirstLogin)
            apply()
        }
    }

}