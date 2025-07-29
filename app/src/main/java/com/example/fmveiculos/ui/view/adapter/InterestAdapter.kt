package com.example.fmveiculos.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel

class InterestAdapter(
    private val interests: MutableList<InterestModel>,
    private val onItemClick: (InterestModel, String) -> Unit
) : RecyclerView.Adapter<InterestAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val carNameTextView: TextView = itemView.findViewById(R.id.carNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewInterestDate)
        val confirmButton: Button = itemView.findViewById(R.id.buttonConfirmInterest)
        val cancelButton: Button = itemView.findViewById(R.id.buttonConfirmInterest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val interest = interests[position]
        holder.userNameTextView.text = interest.name
        holder.carNameTextView.text = interest.carName
        holder.dateTextView.text = interest.timestamp
        holder.confirmButton.setOnClickListener {
            onItemClick(interest, "confirm")
        }
        holder.cancelButton.setOnClickListener {
            onItemClick(interest, "cancel")
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