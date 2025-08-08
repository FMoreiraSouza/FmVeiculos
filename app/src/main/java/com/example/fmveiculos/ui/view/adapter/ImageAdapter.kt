package com.example.fmveiculos.ui.view.adapter

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
import androidx.appcompat.app.AppCompatActivity
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
            try {
                cars = carRepository.getCars()
                Log.d("ImageAdapter", "Cars loaded: $cars") // Log para depuração
                notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("ImageAdapter", "Error loading cars: ${e.message}", e)
                (context as? AppCompatActivity)?.runOnUiThread {
                    android.widget.Toast.makeText(
                        context,
                        "Erro ao carregar veículos: ${e.message}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getCount(): Int = cars.size

    override fun getItem(position: Int): Any = cars[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            try {
                view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } catch (e: Exception) {
                Log.e("ImageAdapter", "Error inflating view: ${e.message}", e)
                return View(context) // Retorna uma view vazia para evitar crash
            }
        } else {
            viewHolder = view.tag as ViewHolder
        }

        try {
            val car = cars[position]
            viewHolder.bind(car)

            // Configurar animação de clique
            val clickAnimation = AnimationUtils.loadAnimation(context, R.anim.button_highlight)
            clickAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    view?.clearAnimation()
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })

            view?.setOnClickListener {
                try {
                    it.startAnimation(clickAnimation)
                    val intent = CarModel.createIntent(context, car, hasWhatsAppLayout)
                    Log.d("ImageAdapter", "Navigating to activity with intent: $intent, car: $car")

                    // Verificar se o contexto é uma Activity para usar transições
                    if (context is Activity) {
                        val options = ActivityOptions.makeSceneTransitionAnimation(context)
                        ActivityCompat.startActivity(context, intent, options.toBundle())
                    } else {
                        context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.e("ImageAdapter", "Error on click: ${e.message}", e)
                    (context as? AppCompatActivity)?.runOnUiThread {
                        android.widget.Toast.makeText(
                            context,
                            "Erro ao navegar: ${e.message}",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ImageAdapter", "Error binding view: ${e.message}", e)
        }

        return view ?: View(context) // Retorna uma view vazia em caso de erro
    }

    private inner class ViewHolder(view: View) {
        private val carImage: ImageView = view.findViewById(R.id.carImageView)
        private val carName: TextView = view.findViewById(R.id.carNameTextView)

        fun bind(car: CarModel) {
            try {
                if (car.imageResource.isNotEmpty()) {
                    Glide.with(carImage.context)
                        .load(car.imageResource)
                        .fitCenter()
                        .into(carImage)
                }
                carName.text = car.name ?: "Nome não disponível"
            } catch (e: Exception) {
                Log.e("ViewHolder", "Error binding data: ${e.message}", e)
                carName.text = "Erro"
            }
        }
    }
}