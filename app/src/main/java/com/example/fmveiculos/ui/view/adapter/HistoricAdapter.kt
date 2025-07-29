package com.example.fmveiculos.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel

class HistoricAdapter(
    private val interests: MutableList<InterestModel>,
    private val onItemClick: (InterestModel) -> Unit
) : RecyclerView.Adapter<HistoricAdapter.HistoricViewHolder>() {

    inner class HistoricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carNameTextView: TextView = itemView.findViewById(R.id.carNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewInterestDate)
        val statusImageView: ImageView = itemView.findViewById(R.id.imageStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_layout, parent, false)
        return HistoricViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricViewHolder, position: Int) {
        val interest = interests[position]
        holder.carNameTextView.text = interest.carName
        holder.dateTextView.text = interest.timestamp
        holder.statusImageView.setImageResource(
            when (interest.status) {
                "Confirmado" -> R.drawable.ic_confirmation_24
                "Cancelado" -> R.drawable.ic_cancel_24
                else -> R.drawable.ic_pending_24
            }
        )
        holder.itemView.setOnClickListener {
            onItemClick(interest)
        }
    }

    override fun getItemCount(): Int = interests.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateInterests(newInterests: List<InterestModel>) {
        interests.clear()
        interests.addAll(newInterests)
        notifyDataSetChanged()
    }
}