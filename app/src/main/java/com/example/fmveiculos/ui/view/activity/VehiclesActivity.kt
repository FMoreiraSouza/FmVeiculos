package com.example.fmveiculos.ui.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.presenter.contract.VehiclesContract
import com.example.fmveiculos.presenter.impl.VehiclesPresenter
import com.example.fmveiculos.ui.view.adapter.ImageAdapter
import com.example.fmveiculos.utils.Navigator
import android.widget.GridView

class VehiclesActivity : AppCompatActivity(), VehiclesContract.View {

    private lateinit var presenter: VehiclesPresenter
    private lateinit var toolbar: Toolbar
    private lateinit var gridView: GridView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        presenter = VehiclesPresenter()

        toolbar = findViewById(R.id.toolbar)
        gridView = findViewById(R.id.gridView)

        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        gridView.adapter = ImageAdapter(this, false)
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }

    override fun showError(message: String) {

    }
}