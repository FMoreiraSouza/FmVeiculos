package com.example.fmveiculos.ui.adapter

import InterestModel
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.fmveiculos.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class InterestAdapter(private val context: Context) : BaseAdapter() {

    private val interests = mutableListOf<InterestModel>()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        firestore.collection("interests").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val interest = document.toObject(InterestModel::class.java)
                    interests.add(interest)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("InterestAdapter", "Error fetching data", exception)
            }
    }

    override fun getCount(): Int {
        return interests.size
    }

    override fun getItem(position: Int): Any {
        return interests[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

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

        viewHolder.clientNameTextView.text = interest.clientName
        viewHolder.carNameTextView.text = interest.carName
        viewHolder.carPriceTextView.text = "RS " + String.format("%.2f", interest.carPrice)

        // Formatando a data
        val dateFormatFirebase = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        try {
            val date = dateFormatFirebase.parse(interest.timestamp)
            if (date != null) {
                val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                val formattedDate = dateFormat.format(date)
                viewHolder.interestDateTextView.text = formattedDate
            } else {
                viewHolder.interestDateTextView.text = "Data Inválida"
            }
        } catch (e: Exception) {
            Log.e("InterestAdapter", "Error parsing or formatting date", e)
            viewHolder.interestDateTextView.text = "Data Inválida"
        }

        // Configurando o clique do botão
        viewHolder.buttonConfirmInterest.setOnClickListener {
            showConfirmationPopup(interest, view)
        }

        return view
    }

    private fun showConfirmationPopup(interest: InterestModel, convertView: View) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.confirmation_popup, null)

        // Configurando o PopupWindow com largura e altura adequadas
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)

        popupWindow.showAtLocation(convertView, Gravity.CENTER, 0, 0)

        val buttonYes = popupView.findViewById<Button>(R.id.buttonYes)
        val buttonNo = popupView.findViewById<Button>(R.id.buttonNo)

        buttonYes.setOnClickListener {
            saveDataWithSharedPreferences(interest)
            popupWindow.dismiss()
        }

        buttonNo.setOnClickListener {
            interests.remove(interest)
            notifyDataSetChanged()
            popupWindow.dismiss()
        }
    }


    private fun saveDataWithSharedPreferences(interest: InterestModel) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("clientName", interest.clientName)
        editor.putString("carName", interest.carName)
        editor.putFloat("carPrice", interest.carPrice.toFloat())
        editor.putString("timestamp", interest.timestamp)
        editor.apply()

        Toast.makeText(context, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
    }

    private class ViewHolder(view: View) {
        val clientNameTextView: TextView = view.findViewById(R.id.textViewClientName)
        val carNameTextView: TextView = view.findViewById(R.id.textViewCarName)
        val carPriceTextView: TextView = view.findViewById(R.id.textViewCarPrice)
        val interestDateTextView: TextView = view.findViewById(R.id.textViewInterestDate)
        val buttonConfirmInterest: Button = view.findViewById(R.id.buttonConfirmInterest)
    }
}
