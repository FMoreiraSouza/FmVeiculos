package com.example.fmveiculos.ui.view.home
import com.example.fmveiculos.ui.adapter.ImageAdapter
import android.annotation.SuppressLint
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
import com.example.fmveiculos.ui.view.auth.LoginActivity

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeClientActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

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
                    auth.signOut()
                    val intent = Intent(this@HomeClientActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        val logoText = navigationView.getHeaderView(0).findViewById<TextView>(R.id.fmVehiclesTextView)
        val text = "FMVeículos"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#A9A9A9")), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // Cor cinza platinado
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF4500")), 2, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // Cor vermelho metálico
        logoText.setText(spannable, TextView.BufferType.SPANNABLE)

        val gridView: GridView = findViewById(R.id.vehiclesGridView)
        val adapter = ImageAdapter(this, true)
        gridView.adapter = adapter
    }
}
