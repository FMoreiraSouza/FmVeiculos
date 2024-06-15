package com.example.fmveiculos.view.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fmveiculos.R
import com.example.fmveiculos.view.auth.LoginActivity
import com.example.fmveiculos.viewModel.home.SquareAdapter
import com.google.android.material.navigation.NavigationView

class HomeAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.inflateHeaderView(R.layout.nav_header_layout)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    val intent = Intent(this@HomeAdminActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        val logoText = navigationView.getHeaderView(0).findViewById<TextView>(R.id.fmVehiclesTextView)
        val text = "Administrador"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#A9A9A9")), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        logoText.setText(spannable, TextView.BufferType.SPANNABLE)

        val gridView = findViewById<GridView>(R.id.gridView)
        gridView.adapter = SquareAdapter(this)
    }
}
