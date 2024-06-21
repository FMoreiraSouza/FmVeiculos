package com.example.fmveiculos.ui.view.interests

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.adapter.HistoricAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.example.fmveiculos.model.InterestModel
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.ui.view.home.HomeClientActivity
import com.example.fmveiculos.utils.Navigator
import java.text.SimpleDateFormat
import java.util.*

class HistoricAdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoricAdapter
    private val interestsList = mutableListOf<InterestModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_admin)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
        }

        recyclerView = findViewById(R.id.recyclerViewInterests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoricAdapter(interestsList)
        recyclerView.adapter = adapter

        fetchInterestsFromFirestore()
    }

    private fun fetchInterestsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("interests")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val id = ""
                    val userId = ""
                    val carId = ""
                    val clientName = ""
                    val carName = document.getString("carName") ?: ""
                    val carPrice = document.getDouble("carPrice") ?: 0.0
                    val timestamp = document.getString("timestamp") ?: ""
                    val status = document.getString("status") ?: ""

                    // Formatar a data
                    val formattedTimestamp = formatFirebaseTimestamp(timestamp)

                    val interest = InterestModel(id, userId, carId, clientName, carName, carPrice, formattedTimestamp, status)
                    interestsList.add(interest)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {}
    }

    private fun formatFirebaseTimestamp(firebaseTimestamp: String): String {
        val dateFormatFirebase = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

        return try {
            val date = dateFormatFirebase.parse(firebaseTimestamp)
            if (date != null) {
                dateFormat.format(date)
            } else {
                "Data Inválida"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Data Inválida"
        }
    }
}
