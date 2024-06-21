package com.example.fmveiculos.ui.adapter

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import com.example.fmveiculos.R
import com.example.fmveiculos.model.InterestModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InterestAdapter(private val context: Context) : BaseAdapter() {

    private val interests = mutableListOf<InterestModel>()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        firestore.collection("interests")
            .whereEqualTo("status", "Pendente")
            .get()
            .addOnSuccessListener { documents ->
                interests.clear()
                for (document in documents) {
                    val interest = document.toObject(InterestModel::class.java)
                    interests.add(interest)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("com.example.fmveiculos.ui.adapter.InterestAdapter", "Error fetching data", exception)
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

        viewHolder.clientNameTextView.text = interest.name
        viewHolder.carNameTextView.text = interest.carName
        viewHolder.carPriceTextView.text = "RS " + String.format("%.2f", interest.carPrice)

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
            Log.e("com.example.fmveiculos.ui.adapter.InterestAdapter", "Error parsing or formatting date", e)
            viewHolder.interestDateTextView.text = "Data Inválida"
        }

        viewHolder.buttonConfirmInterest.setOnClickListener {
            showConfirmationPopup(interest, view)
        }

        return view
    }

    private fun showConfirmationPopup(interest: InterestModel, convertView: View) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.confirmation_popup, null)

        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)

        popupWindow.showAtLocation(convertView, Gravity.CENTER, 0, 0)

        val buttonYes = popupView.findViewById<Button>(R.id.buttonYes)
        val buttonNo = popupView.findViewById<Button>(R.id.buttonNo)

        buttonYes.setOnClickListener {
            firestore.collection("cars").document(interest.carId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val currentQuantity = documentSnapshot.getLong("quantity") ?: 0
                    if (currentQuantity > 0) {
                        // Decrementa a quantidade
                        val newQuantity = currentQuantity - 1
                        // Atualiza a quantidade no Firestore
                        firestore.collection("cars").document(interest.carId)
                            .update("quantity", newQuantity)
                            .addOnSuccessListener {
                                Log.d("InterestAdapter", "Quantity decremented successfully")
                                // Atualiza o status do interesse para "Confirmado" no Firestore
                                firestore.runTransaction { transaction ->
                                    // Referência para o documento de confirmações do mês atual
                                    val currentMonth = SimpleDateFormat("yyyy-MM").format(Date())
                                    val confirmationDocRef = firestore.collection("confirmations").document(currentMonth)

                                    // Lógica para incrementar o contador de confirmações no documento
                                    val snapshot = transaction.get(confirmationDocRef)
                                    if (snapshot.exists()) {
                                        val currentCount = snapshot.getLong("count") ?: 0
                                        transaction.update(confirmationDocRef, "count", currentCount + 1)
                                    } else {
                                        val data = hashMapOf(
                                            "count" to 1,
                                            "month" to currentMonth
                                        )
                                        transaction.set(confirmationDocRef, data)
                                    }

                                    // Atualiza o status do interesse para "Confirmado" no documento de interesses
                                    transaction.update(
                                        firestore.collection("interests").document(interest.id),
                                        "status",
                                        "Confirmado"
                                    )

                                    // Atualiza o nome do carro no documento de confirmações
                                    transaction.update(confirmationDocRef, "carName", interest.carName)
                                }.addOnSuccessListener {
                                    Log.d("InterestAdapter", "Status updated successfully")
                                    interest.status = "Confirmado"
                                    interests.remove(interest)
                                    notifyDataSetChanged()
                                    popupWindow.dismiss()

                                    // Mostra um toast personalizado indicando que o pedido foi confirmado
                                    showCustomToast("Seu pedido foi confirmado com sucesso e será processado em nosso sistema central!")
                                }.addOnFailureListener { e ->
                                    Log.e("InterestAdapter", "Error updating status or confirmation count", e)
                                    Toast.makeText(context, "Erro ao confirmar interesse", Toast.LENGTH_SHORT).show()
                                    popupWindow.dismiss()
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("InterestAdapter", "Error decrementing quantity", e)
                                Toast.makeText(context, "Erro ao decrementar quantidade", Toast.LENGTH_SHORT).show()
                                popupWindow.dismiss()
                            }
                    } else {
                        Toast.makeText(context, "Quantidade insuficiente do carro", Toast.LENGTH_SHORT).show()
                        popupWindow.dismiss()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("InterestAdapter", "Error fetching car quantity", e)
                    Toast.makeText(context, "Erro ao obter quantidade do carro", Toast.LENGTH_SHORT).show()
                    popupWindow.dismiss()
                }
        }


        buttonNo.setOnClickListener {
            firestore.collection("interests").document(interest.id).update("status", "Cancelado")
                .addOnSuccessListener {
                    Log.d("com.example.fmveiculos.ui.adapter.InterestAdapter", "Status updated successfully")
                    interest.status = "Cancelado"
                    interests.remove(interest)
                    notifyDataSetChanged()
                    popupWindow.dismiss()

                    // Mostra um toast indicando que o interesse foi cancelado
                    Toast.makeText(context, "Interesse cancelado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("com.example.fmveiculos.ui.adapter.InterestAdapter", "Error updating status", e)
                    Toast.makeText(context, "Erro ao cancelar interesse", Toast.LENGTH_SHORT).show()
                    popupWindow.dismiss()
                }
        }
    }

    private fun showCustomToast(message: String) {
        // Infla o layout customizado
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.toast_custom, null)

        // Configura o texto da mensagem no TextView do layout customizado
        val textMessage = layout.findViewById<TextView>(R.id.textMessage)
        textMessage.text = message

        // Cria e configura o Toast
        with (Toast(context)) {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }


    private class ViewHolder(view: View) {
        val clientNameTextView: TextView = view.findViewById(R.id.textViewClientName)
        val carNameTextView: TextView = view.findViewById(R.id.textViewCarName)
        val carPriceTextView: TextView = view.findViewById(R.id.textViewCarPrice)
        val interestDateTextView: TextView = view.findViewById(R.id.textViewInterestDate)
        val buttonConfirmInterest: Button = view.findViewById(R.id.buttonConfirmInterest)
    }
}
