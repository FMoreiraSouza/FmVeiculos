package com.example.fmveiculos.ui.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
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
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        } else {
            viewHolder = view.tag as ViewHolder
        }

        val car = cars[position]
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
            val intent = CarModel.createIntent(context, car, hasWhatsAppLayout)

            if (context is Activity) {
                val options = ActivityOptions.makeSceneTransitionAnimation(context)
                ActivityCompat.startActivity(context, intent, options.toBundle())
            } else {
                context.startActivity(intent)
            }
        }
        return view ?: View(context)
    }

    private inner class ViewHolder(view: View) {
        private val carImage: ImageView = view.findViewById(R.id.carImageView)
        private val carName: TextView = view.findViewById(R.id.carNameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(car: CarModel) {
            if (car.imageResource.isNotEmpty()) {
                Glide.with(carImage.context)
                    .load(car.imageResource)
                    .fitCenter()
                    .into(carImage)
            }
            carName.text = car.name
        }
    }
}