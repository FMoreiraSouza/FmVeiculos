package com.example.fmveiculos.viewModel.home

import CarModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.google.firebase.firestore.FirebaseFirestore

class ImageAdapter(private val context: Context) : BaseAdapter() {

    private val carList = mutableListOf<CarModel>()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        firestore.collection("cars").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val car = document.toObject(CarModel::class.java)
                    carList.add(car)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
            }
    }

    override fun getCount(): Int {
        return carList.size
    }

    override fun getItem(position: Int): Any {
        return carList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val car = getItem(position) as CarModel
        viewHolder.bind(car)

        view!!.setOnClickListener {
            val intent = CarModel.createIntent(context, car)
            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val carImage: ImageView = view.findViewById(R.id.carImageView)
        val carName: TextView = view.findViewById(R.id.carNameTextView)

        fun bind(car: CarModel) {
            Glide.with(carImage.context)
                .load(car.imageResource)
                .into(carImage)

            carName.text = car.name
        }
    }
}
