package com.example.fmveiculos.ui.adapter

import com.example.fmveiculos.model.InterestModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R

class InterestListAdapter(private val interests: List<InterestModel>) :
    RecyclerView.Adapter<InterestListAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.carNameTextView)
        private val carPriceTextView: TextView = itemView.findViewById(R.id.carPriceTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)

        fun bind(interest: InterestModel) {
            carNameTextView.text = "Modelo: ${interest.carName}"
            carPriceTextView.text = "Preço: ${interest.carPrice}"
            timestampTextView.text = "Data e hora: ${interest.timestamp}"
            statusTextView.text = "Status: ${interest.status}"
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
