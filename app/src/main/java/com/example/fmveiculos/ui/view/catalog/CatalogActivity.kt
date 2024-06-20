package com.example.fmveiculos.ui.view.catalog

import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.adapter.ImageAdapter
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.utils.Navigator

class CatalogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
        }

        val gridView: GridView = findViewById(R.id.vehiclesAdminGridView)
        val adapter = ImageAdapter(this, false)
        gridView.adapter = adapter
    }
}