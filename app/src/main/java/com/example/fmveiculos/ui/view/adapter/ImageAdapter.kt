package com.example.fmveiculos.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.CarModel
import com.example.fmveiculos.data.repository.CarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageAdapter(
    private val context: Context,
    private val hasWhatsAppLayout: Boolean
) : BaseAdapter() {

    private val carRepository = CarRepository()
    private var cars: List<CarModel> = emptyList()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            cars = carRepository.getCars()
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = cars.size

    override fun getItem(position: Int): Any = cars[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
        val car = cars[position]
        val imageView = view.findViewById<ImageView>(R.id.imageViewProduto)
        val nameTextView = view.findViewById<TextView>(R.id.textViewName)

        Glide.with(context).load(car.imageResource).fitCenter().into(imageView)
        nameTextView.text = car.name

        view.setOnClickListener {
            val intent = CarModel.createIntent(context, car, hasWhatsAppLayout)
            context.startActivity(intent)
        }

        return view
    }
}