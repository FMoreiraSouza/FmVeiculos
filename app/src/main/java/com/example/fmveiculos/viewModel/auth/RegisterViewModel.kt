package com.example.fmveiculos.viewModel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Utilizando um enum para diferenciar os tipos de erros
    enum class RegisterError {
        EMAIL_EXISTS,
        CPF_EXISTS,
        DOMAIN_NOT_ALLOWED,
        OTHER
    }

    private val _registerResult = MutableLiveData<Pair<Boolean, RegisterError?>>()
    val registerResult: LiveData<Pair<Boolean, RegisterError?>> = _registerResult

    fun registerUser(email: String, password: String, cpf: String) {
        val isFmVeiculosDomain = email.endsWith("@fmveiculos.com")

        if (isFmVeiculosDomain) {
            // Domínio fmveiculos.com não é permitido
            _registerResult.value = Pair(false, RegisterError.DOMAIN_NOT_ALLOWED)
            return
        }

        // Verificar se o e-mail já está cadastrado no Firebase Authentication
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList()
                    val emailExists = signInMethods.isNotEmpty()
                    if (emailExists) {
                        // E-mail já cadastrado
                        _registerResult.value = Pair(false, RegisterError.EMAIL_EXISTS)
                    } else {
                        // Verificar se o CPF já está cadastrado no Firestore
                        db.collection("userInfo")
                            .whereEqualTo("cpf", cpf)
                            .get()
                            .addOnCompleteListener { cpfTask ->
                                if (cpfTask.isSuccessful) {
                                    if (!cpfTask.result.isEmpty) {
                                        // CPF já cadastrado
                                        _registerResult.value = Pair(false, RegisterError.CPF_EXISTS)
                                    } else {
                                        // E-mail e CPF não cadastrados, continuar o registro
                                        auth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { signUpTask ->
                                                _registerResult.value = Pair(signUpTask.isSuccessful, null)
                                            }
                                    }
                                } else {
                                    // Exceção ao verificar o CPF
                                    _registerResult.value = Pair(false, RegisterError.OTHER)
                                }
                            }
                    }
                } else {
                    // Exceção ao verificar o e-mail
                    _registerResult.value = Pair(false, RegisterError.OTHER)
                }
            }
    }
}
