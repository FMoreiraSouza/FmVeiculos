package com.example.fmveiculos.viewModel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult
    fun registerUser(email: String, password: String) {
        if (email.endsWith("@fmveiculos.com")) {
            _registerResult.value = false
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _registerResult.value = true
                } else {
                    Log.w("RegisterViewModel", "signUpWithEmailAndPassword: failure", task.exception)
                    _registerResult.value = false
                }
            }
    }

}