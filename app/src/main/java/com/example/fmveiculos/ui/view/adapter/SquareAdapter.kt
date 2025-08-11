package com.example.fmveiculos.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.view.activity.DashboardActivity
import com.example.fmveiculos.ui.view.activity.InterestActivity
import com.example.fmveiculos.ui.view.activity.RestockingActivity
import com.example.fmveiculos.ui.view.activity.VehiclesActivity
import com.example.fmveiculos.utils.Navigator

class SquareAdapter(private val context: Context) : BaseAdapter() {

    private val squareItems = listOf(
        SquareItem(R.drawable.car_restocking, "Reposição de Estoque", RestockingActivity::class.java),
        SquareItem(R.drawable.car_dealership, "Catálogo de Veículos", VehiclesActivity::class.java),
        SquareItem(R.drawable.sales_register, "Registro de Interesses", InterestActivity::class.java),
        SquareItem(R.drawable.sales_report, "Relatório de Vendas", DashboardActivity::class.java)
    )

    data class SquareItem(val imageRes: Int, val title: String, val activityClass: Class<*>)

    override fun getCount(): Int = squareItems.size

    override fun getItem(position: Int): Any = squareItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.square_item, parent, false)
        val item = squareItems[position]

        val imageView = view.findViewById<ImageView>(R.id.squareIcon)
        val textView = view.findViewById<TextView>(R.id.squareName)

        imageView.setImageResource(item.imageRes)
        textView.text = item.title

        val squareView = view.findViewById<View>(R.id.squareView)
        squareView.setBackgroundColor(when (position) {
            0 -> android.graphics.Color.RED
            1 -> android.graphics.Color.GRAY
            2 -> android.graphics.Color.GRAY
            3 -> android.graphics.Color.RED
            else -> android.graphics.Color.TRANSPARENT
        })

        view.setOnClickListener {
            Navigator().navigateToActivity(context, item.activityClass)
        }

        return view
    }
}