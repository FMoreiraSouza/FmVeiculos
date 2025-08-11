package com.example.fmveiculos.ui.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel
import java.text.SimpleDateFormat
import java.util.Locale

class InterestAdapter(
    private val context: Context,
    private val interests: MutableList<InterestModel>,
    private val onItemClick: (InterestModel, String) -> Unit
) : BaseAdapter() {

    private class ViewHolder(view: View) {
        val userNameTextView: TextView = view.findViewById(R.id.textViewClientName)
        val carNameTextView: TextView = view.findViewById(R.id.textViewCarName)
        val dateTextView: TextView = view.findViewById(R.id.textViewInterestDate)
        val carPriceTextView: TextView = view.findViewById(R.id.textViewCarPrice)
        val confirmButton: Button = view.findViewById(R.id.buttonConfirmInterest)
    }

    override fun getCount(): Int = interests.size

    override fun getItem(position: Int): Any = interests[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.card_item_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val interest = getItem(position) as InterestModel
        viewHolder.userNameTextView.text = interest.name
        viewHolder.carNameTextView.text = interest.carName
        viewHolder.carPriceTextView.text = "R$ ${String.format("%.2f", interest.carPrice)}"
        viewHolder.dateTextView.text = formatTimestamp(interest.timestamp)
        viewHolder.confirmButton.setOnClickListener {
            onItemClick(interest, "confirm")
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateInterests(newInterests: List<InterestModel>) {
        interests.clear()
        interests.addAll(newInterests)
        notifyDataSetChanged()
    }

    private fun formatTimestamp(firebaseTimestamp: String): String {
        val dateFormatFirebase = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return try {
            val date = dateFormatFirebase.parse(firebaseTimestamp)
            date?.let { dateFormat.format(it) } ?: "Data Inválida"
        } catch (_: Exception) {
            "Data Inválida"
        }
    }
}