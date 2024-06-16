package com.example.fmveiculos.model

import android.content.Context
import android.content.Intent
import com.example.fmveiculos.view.dealership.CarDetailsActivity
data class UserModel(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var cpf: String = "",
    var cep: String = "",
) {
    companion object {
        fun createIntent(context: Context, user: UserModel): Intent {
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra("userName", user.name)
            intent.putExtra("userEmail", user.email)
            intent.putExtra("userPassword", user.password)
            intent.putExtra("userCPF", user.cpf)
            intent.putExtra("userCEP", user.cep)
            return intent
        }
    }
}

