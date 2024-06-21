package com.example.fmveiculos.ui.adapter

import CarModel
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.fmveiculos.R
import com.google.firebase.firestore.FirebaseFirestore

class ImageAdapter(private val context: Context, private val hasWhatsappLayout: Boolean) :
    BaseAdapter() {

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
                view =
                    LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
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

            val clickAnimation = AnimationUtils.loadAnimation(context, R.anim.button_highlight)

            clickAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    view?.clearAnimation()
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })

            view?.setOnClickListener {
                it.startAnimation(clickAnimation)

                val intent = CarModel.createIntent(context, car, hasWhatsappLayout)
                val options =
                    ActivityOptions.makeSceneTransitionAnimation(context as Activity)
                ActivityCompat.startActivity(context, intent, options.toBundle())

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
