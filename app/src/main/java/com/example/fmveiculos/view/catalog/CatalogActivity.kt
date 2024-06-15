package com.example.fmveiculos.view.catalog

import com.example.fmveiculos.viewModel.home.ImageAdapter
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.fmveiculos.R

class CatalogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_catalog)

        val gridView: GridView = findViewById(R.id.vehiclesGridView)
        val adapter = ImageAdapter(this)
        gridView.adapter = adapter
    }
}