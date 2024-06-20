package com.example.fmveiculos.ui.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.view.catalog.CatalogActivity
import com.example.fmveiculos.ui.view.dashboard.DashboardActivity
import com.example.fmveiculos.ui.view.interests.InterestActivity
import com.example.fmveiculos.ui.view.restocking.RestockingActivity

class SquareAdapter(private val context: Context) : BaseAdapter() {

    private val squares = listOf(Color.RED, Color.GRAY, Color.GRAY, Color.RED)
    private val icons = listOf(
        R.drawable.car_dealership,
        R.drawable.car_restocking,
        R.drawable.sales_report,
        R.drawable.sales_register
    )
    private val names = listOf("Veículos", "Reposição", "Dashboard", "Interesses")

    override fun getCount(): Int {
        return squares.size
    }

    override fun getItem(position: Int): Any {
        return squares[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val squareColor = squares[position]
        val iconResId = icons[position]
        val name = names[position]

        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.square_item, parent, false)
        } else {
            view = convertView
        }

        val squareName = view.findViewById<TextView>(R.id.squareName)
        val squareView = view.findViewById<View>(R.id.squareView)
        val squareIcon = view.findViewById<ImageView>(R.id.squareIcon)

        squareName.text = name
        squareView.setBackgroundColor(squareColor)
        squareIcon.setImageResource(iconResId)

        // Configuração da animação de clique
        val clickAnimation = AnimationUtils.loadAnimation(context, R.anim.button_highlight)
        view.setOnClickListener {
            it.startAnimation(clickAnimation) // Inicia a animação quando o item é clicado

            // Lógica de navegação conforme o nome do item clicado
            when (name) {
                "Veículos" -> {
                    val intent = Intent(context, CatalogActivity::class.java)

                    // Configurar a transição de cena personalizada
                    val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity)

                    // Iniciar a nova atividade com a transição de cena personalizada
                    ActivityCompat.startActivity(context, intent, options.toBundle())
                }
                "Reposição" -> {
                    val intent = Intent(context, RestockingActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity)
                    ActivityCompat.startActivity(context, intent, options.toBundle())
                }
                "Dashboard" -> {
                    val intent = Intent(context, DashboardActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity)
                    ActivityCompat.startActivity(context, intent, options.toBundle())
                }
                "Interesses" -> {
                    val intent = Intent(context, InterestActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity)
                    ActivityCompat.startActivity(context, intent, options.toBundle())
                }
            }
        }

        return view
    }
}
