package com.example.fmveiculos.ui.view.interests

import com.example.fmveiculos.ui.adapter.InterestAdapter
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.utils.Navigator

class InterestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
        }

        val gridViewInterests: GridView = findViewById(R.id.gridViewInterests)
        val adapter = InterestAdapter(this)
        gridViewInterests.adapter = adapter
    }
}
