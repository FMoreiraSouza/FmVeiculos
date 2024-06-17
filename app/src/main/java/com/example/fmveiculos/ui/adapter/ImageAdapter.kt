package com.example.fmveiculos.ui.adapter

import CarModel
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.google.firebase.firestore.FirebaseFirestore

class ImageAdapter(private val context: Context, private val hasWhatsappLayout: Boolean) : BaseAdapter() {

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
                Log.e("ImageAdapter", "Error fetching data", exception)
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            try {
                Log.d("ImageAdapter", "Inflating new view for position $position")
                view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
                Log.d("ImageAdapter", "View inflated successfully for position $position")
            } catch (e: Exception) {
                Log.e("ImageAdapter", "Error inflating view", e)
                return null
            }
        } else {
            viewHolder = view.tag as ViewHolder
        }

        try {
            val car = getItem(position) as CarModel
            viewHolder.bind(car)

            if (view != null) {
                view.setOnClickListener {
                    val intent = CarModel.createIntent(context, car, hasWhatsappLayout)
                    context.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Log.e("ImageAdapter", "Error binding view", e)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val carImage: ImageView = view.findViewById(R.id.carImageView)
        val carName: TextView = view.findViewById(R.id.carNameTextView)

        fun bind(car: CarModel) {
            try {
                Glide.with(carImage.context)
                    .load(car.imageResource)
                    .into(carImage)
                carName.text = car.name
            } catch (e: Exception) {
                Log.e("ViewHolder", "Error binding data", e)
            }
        }
    }
}
