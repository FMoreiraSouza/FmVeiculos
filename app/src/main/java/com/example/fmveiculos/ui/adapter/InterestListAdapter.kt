package com.example.fmveiculos.ui.adapter

import android.graphics.PorterDuff
import com.example.fmveiculos.model.InterestModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R

class InterestListAdapter(private val interests: List<InterestModel>) :
    RecyclerView.Adapter<InterestListAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.textCarName)
        private val statusTextView: TextView = itemView.findViewById(R.id.textStatus)
        private val statusImageView: ImageView = itemView.findViewById(R.id.imageStatus)
        private val carPriceTextView: TextView = itemView.findViewById(R.id.textCarPrice)
        private val timestampTextView: TextView = itemView.findViewById(R.id.textTimestamp)

        fun bind(interest: InterestModel) {
            carNameTextView.text = "Modelo: ${interest.carName}"
            carPriceTextView.text = "Preço: ${interest.carPrice}"
            timestampTextView.text = "Data e hora: ${interest.timestamp}"
            statusTextView.text = "Status: ${interest.status}"

            // Define a imagem com base no status
            when (interest.status) {
                "Cancelado" -> {
                    statusImageView.setImageResource(R.drawable.ic_cancel_24)
                    statusImageView.setColorFilter(itemView.context.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN)
                }
                "Pendente" -> {
                    statusImageView.setImageResource(R.drawable.ic_pending_24)
                    statusImageView.setColorFilter(itemView.context.getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_IN)
                }
                "Confirmado" -> {
                    statusImageView.setImageResource(R.drawable.ic_confirmation_24)
                    statusImageView.setColorFilter(itemView.context.getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_IN)
                }
                else -> false

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val interest = interests[position]
        holder.bind(interest)
    }

    override fun getItemCount(): Int {
        return interests.size
    }
}
