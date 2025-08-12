package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.UserInfoModel
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.presenter.contract.SettingsContract
import com.example.fmveiculos.presenter.impl.SettingsPresenter
import com.example.fmveiculos.utils.Navigator
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity(), SettingsContract.View {

    private lateinit var presenter: SettingsPresenter
    private lateinit var nameTextView: TextView
    private lateinit var cpfTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var stateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        presenter = SettingsPresenter(this, AuthRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        nameTextView = findViewById(R.id.textViewName)
        cpfTextView = findViewById(R.id.textViewCPF)
        cityTextView = findViewById(R.id.textViewCity)
        stateTextView = findViewById(R.id.textViewState)
    }

    override fun onStart() {
        super.onStart()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        presenter.loadUserInfo(userId)
    }

    @SuppressLint("SetTextI18n")
    override fun displayUserInfo(userInfo: UserInfoModel) {
        nameTextView.text = "Nome: ${userInfo.name}"
        cpfTextView.text = "CPF: ${userInfo.cpf}"
        cityTextView.text = "Cidade: ${userInfo.city}"
        stateTextView.text = "Estado: ${userInfo.state}"
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        val originActivity = intent.getStringExtra("originActivity")
        val destination = if (originActivity == HomeAdminActivity::class.java.name) {
            HomeAdminActivity::class.java
        } else {
            HomeClientActivity::class.java
        }
        Navigator().navigateToActivity(this, destination)
    }
}