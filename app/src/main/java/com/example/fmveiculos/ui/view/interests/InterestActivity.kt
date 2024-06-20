package com.example.fmveiculos.ui.view.interests

import InterestAdapter
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.utils.Navigator
import com.google.android.material.navigation.NavigationView

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
