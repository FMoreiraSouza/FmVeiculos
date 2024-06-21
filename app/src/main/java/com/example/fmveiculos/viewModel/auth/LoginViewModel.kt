package com.example.fmveiculos.viewModel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = true
                } else {
                    Log.w(
                        "LoginViewModel",
                        "signInWithEmailAndPassword: failure",
                        task.exception
                    )
                    _loginResult.value = false
                }
            }
    }

    fun checkLoggedUser() {
        if (auth.currentUser != null) {
            _navigateToHome.value = true
            return
        }
    }
}
