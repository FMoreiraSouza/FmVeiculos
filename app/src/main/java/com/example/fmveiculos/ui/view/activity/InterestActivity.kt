package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
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
    private var currentDialog: Dialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        presenter = InterestPresenter(this, InterestRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        gridView = findViewById(R.id.gridViewInterests)
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
        presenter.loadPendingInterests()
    }

    override fun displayInterests(interests: List<InterestModel>) {
        adapter.updateInterests(interests)
    }

    override fun showConfirmSuccess() {
        Toast.makeText(this, "Interesse confirmado com sucesso!", Toast.LENGTH_SHORT).show()
        currentDialog?.dismiss()
        currentDialog = null
    }

    override fun showCancelSuccess() {
        Toast.makeText(this, "Interesse cancelado com sucesso!", Toast.LENGTH_SHORT).show()
        currentDialog?.dismiss()
        currentDialog = null
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        currentDialog?.dismiss()
        currentDialog = null
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId", "InflateParams")
    private fun showConfirmationPopup(interest: InterestModel) {
        val dialog = Dialog(this)
        currentDialog = dialog
        val view = layoutInflater.inflate(R.layout.confirmation_popup, null)
        val confirmButton = view.findViewById<Button>(R.id.buttonYes)
        val cancelButton = view.findViewById<Button>(R.id.buttonNo)

        confirmButton.setOnClickListener {
            presenter.confirmInterest(interest)
        }
        cancelButton.setOnClickListener {
            presenter.cancelInterest(interest.id)
        }

        dialog.setContentView(view)
        dialog.show()
    }
}