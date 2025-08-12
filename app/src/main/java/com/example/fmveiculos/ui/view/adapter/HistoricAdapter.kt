package com.example.fmveiculos.ui.view.adapter

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel

class HistoricAdapter(private val interests: MutableList<InterestModel>) :
    RecyclerView.Adapter<HistoricAdapter.HistoricViewHolder>() {

    inner class HistoricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.textCarName)
        private val statusTextView: TextView = itemView.findViewById(R.id.textStatus)
        private val statusImageView: ImageView = itemView.findViewById(R.id.imageStatus)
        private val carPriceTextView: TextView = itemView.findViewById(R.id.textCarPrice)
        private val timestampTextView: TextView = itemView.findViewById(R.id.textTimestamp)

        @SuppressLint("SetTextI18n")
        fun bind(interest: InterestModel) {
            carNameTextView.text = "Modelo: ${interest.carName}"
            carPriceTextView.text = "Preço: ${interest.carPrice}"
            timestampTextView.text = "Data e hora: ${interest.timestamp}"
            statusTextView.text = "Status: ${interest.status}"

            when (interest.status) {
                "Cancelado" -> {
                    statusImageView.setImageResource(R.drawable.ic_cancel_24)
                    statusImageView.setColorFilter(
                        itemView.context.getColor(android.R.color.holo_red_light),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                "Pendente" -> {
                    statusImageView.setImageResource(R.drawable.ic_pending_24)
                    statusImageView.setColorFilter(
                        itemView.context.getColor(android.R.color.holo_orange_light),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                "Confirmado" -> {
                    statusImageView.setImageResource(R.drawable.ic_confirmation_24)
                    statusImageView.setColorFilter(
                        itemView.context.getColor(android.R.color.holo_green_light),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return HistoricViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricViewHolder, position: Int) {
        val interest = interests[position]
        holder.bind(interest)
    }

    override fun getItemCount(): Int = interests.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateInterests(newInterests: List<InterestModel>) {
        interests.clear()
        interests.addAll(newInterests)
        notifyDataSetChanged()
    }
}