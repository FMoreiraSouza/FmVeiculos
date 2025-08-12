package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.InterestContract
import com.example.fmveiculos.presenter.impl.InterestPresenter
import com.example.fmveiculos.ui.view.adapter.InterestAdapter
import com.example.fmveiculos.utils.Navigator

class InterestActivity : AppCompatActivity(), InterestContract.View {

    private lateinit var presenter: InterestPresenter
    private lateinit var gridView: GridView
    private lateinit var adapter: InterestAdapter
    private var currentDialog: Dialog? = null // Para controlar o Dialog atual

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)
        Log.d("InterestActivity", "onCreate chamado")

        presenter = InterestPresenter(this, InterestRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        Log.d("InterestActivity", "Toolbar: $toolbar")
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        gridView = findViewById(R.id.gridViewInterests)
        Log.d("InterestActivity", "GridView: $gridView")
        adapter = InterestAdapter(this, mutableListOf()) { interest, action ->
            if (action == "confirm") {
                showConfirmationPopup(interest)
            } else {
                presenter.cancelInterest(interest.id)
            }
        }
        gridView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        Log.d("InterestActivity", "onStart: Carregando interesses pendentes")
        presenter.loadPendingInterests()
    }

    override fun displayInterests(interests: List<InterestModel>) {
        Log.d("InterestActivity", "displayInterests: Atualizando lista com ${interests.size} interesses")
        adapter.updateInterests(interests)
    }

    override fun showConfirmSuccess() {
        Log.d("InterestActivity", "showConfirmSuccess: Interesse confirmado com sucesso")
        Toast.makeText(this, "Interesse confirmado com sucesso!", Toast.LENGTH_SHORT).show()
        currentDialog?.dismiss() // Fecha o Dialog após o sucesso
        currentDialog = null
    }

    override fun showCancelSuccess() {
        Log.d("InterestActivity", "showCancelSuccess: Interesse cancelado com sucesso")
        Toast.makeText(this, "Interesse cancelado com sucesso!", Toast.LENGTH_SHORT).show()
        currentDialog?.dismiss() // Fecha o Dialog após o sucesso
        currentDialog = null
    }

    override fun showError(message: String) {
        Log.d("InterestActivity", "showError: Erro - $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        currentDialog?.dismiss() // Fecha o Dialog em caso de erro
        currentDialog = null
    }

    override fun navigateToHome() {
        Log.d("InterestActivity", "navigateToHome: Navegando para HomeAdminActivity")
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId", "InflateParams")
    private fun showConfirmationPopup(interest: InterestModel) {
        Log.d("InterestActivity", "showConfirmationPopup: Exibindo popup para interesse ${interest.id}")
        val dialog = Dialog(this)
        currentDialog = dialog
        val view = layoutInflater.inflate(R.layout.confirmation_popup, null)
        val confirmButton = view.findViewById<Button>(R.id.buttonYes)
        val cancelButton = view.findViewById<Button>(R.id.buttonNo)

        confirmButton.setOnClickListener {
            Log.d("InterestActivity", "showConfirmationPopup: Botão 'Sim' clicado para interesse ${interest.id}")
            presenter.confirmInterest(interest)
        }
        cancelButton.setOnClickListener {
            Log.d("InterestActivity", "showConfirmationPopup: Botão 'Não' clicado para interesse ${interest.id}")
            presenter.cancelInterest(interest.id)
        }

        dialog.setContentView(view)
        dialog.show()
    }
}