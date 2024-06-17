package com.example.fmveiculos.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
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
    private val names = listOf("Catálogo", "Reposição", "Dashboard", "Interesses")

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

        Log.d("SquareAdapter", "Nome do item: $name")

        view.setOnClickListener {
            if (!name.isNullOrEmpty()) {
                when (name) {
                    "Reposição" -> {
                        val intent = Intent(context, RestockingActivity::class.java)
                        context.startActivity(intent)
                    }
                    "Catálogo" -> {
                        val intent = Intent(context, CatalogActivity::class.java)
                        context.startActivity(intent)
                    }
                    "Dashboard" -> {
                        val intent = Intent(context, DashboardActivity::class.java)
                        context.startActivity(intent)
                    }
                    "Interesses" -> {
                        val intent = Intent(context, InterestActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            } else {
                Log.e("SquareAdapter", "Nome do item é nulo ou vazio")
            }
        }

        return view
    }

}
