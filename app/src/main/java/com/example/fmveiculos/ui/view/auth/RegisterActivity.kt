package com.example.fmveiculos.ui.view.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fmveiculos.R
import com.example.fmveiculos.model.UserInfoModel
import com.example.fmveiculos.utils.CpfMask
import com.example.fmveiculos.utils.Navigator
import com.example.fmveiculos.utils.isValidCPF
import com.example.fmveiculos.viewModel.auth.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var finishRegisterButton: Button
    private lateinit var viewModel: RegisterViewModel
    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var cpfField: EditText
    private lateinit var cityField: EditText
    private lateinit var stateField: EditText
    private lateinit var user: UserInfoModel
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, LoginActivity::class.java)
        }

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        finishRegisterButton = findViewById(R.id.buttonFinishRegister)
        nameField = findViewById(R.id.editTextNome)
        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextSenha)
        cpfField = findViewById(R.id.editTextCPF)
        cityField = findViewById(R.id.editTextCity)
        stateField = findViewById(R.id.editTextState)

        cpfField.addTextChangedListener(CpfMask(cpfField))

        finishRegisterButton.setOnClickListener {
            hideKeyboard()

            // Validar campos obrigatórios
            if (emailField.text.isNullOrEmpty() || passwordField.text.isNullOrEmpty() ||
                nameField.text.isNullOrEmpty() || cpfField.text.isNullOrEmpty() ||
                cityField.text.isNullOrEmpty() || stateField.text.isNullOrEmpty()
            ) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Validar formato de e-mail
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString())
                    .matches()
            ) {
                Toast.makeText(this, "E-mail inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar comprimento mínimo da senha
            if (passwordField.text.length < 6) {
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!isValidCPF(cpfField.text.toString())) {
                Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chamar método do ViewModel para registrar o usuário
            viewModel.registerUser(
                emailField.text.toString(),
                passwordField.text.toString(),
                cpfField.text.toString()
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        val registerObserver = Observer<Pair<Boolean, RegisterViewModel.RegisterError?>> { result ->
            val (success, error) = result

            when {
                success -> {
                    Toast.makeText(this, "Cadastro bem sucedido", Toast.LENGTH_LONG).show()
                    setFirstLoginFlag(true)

                    val db = FirebaseFirestore.getInstance()

                    val customer = auth.currentUser?.let {
                        UserInfoModel(
                            id = it.uid,
                            name = nameField.text.toString(),
                            cpf = cpfField.text.toString(),
                            city = cityField.text.toString(),
                            state = stateField.text.toString(),
                        )
                    }
                    if (customer != null) {
                        db.collection("userInfo")
                            .document(auth.currentUser!!.uid)
                            .set(customer)
                            .addOnSuccessListener {
                                Log.d(
                                    "com.example.fmveiculos.ui.view.auth.RegisterActivity",
                                    "Dados do cliente salvos com sucesso no Firestore"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.e(
                                    "com.example.fmveiculos.ui.view.auth.RegisterActivity",
                                    "Erro ao salvar dados do cliente no Firestore",
                                    e
                                )
                            }
                    }

                    val extras = Bundle().apply {
                        putBoolean("CAME_FROM_REGISTER", true)
                    }
                    Navigator().navigateToActivity(this, LoginActivity::class.java, extras)
                    finish()
                }

                error == RegisterViewModel.RegisterError.CPF_EXISTS -> {
                    Toast.makeText(
                        this,
                        "CPF já cadastrado. Por favor, use outro CPF.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                error == RegisterViewModel.RegisterError.DOMAIN_NOT_ALLOWED -> {
                    Toast.makeText(
                        this,
                        "Clientes não podem ser cadastrados no domínio fmveiculos.com",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "E-mail já cadastrado. Por favor, use outro e-mail.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
