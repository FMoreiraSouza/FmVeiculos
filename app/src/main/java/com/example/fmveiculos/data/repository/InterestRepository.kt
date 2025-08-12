package com.example.fmveiculos.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.fmveiculos.data.model.InterestModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InterestRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getInterestsByUser(userId: String): List<InterestModel> {
        return try {
            val snapshot = db.collection("interests").whereEqualTo("userId", userId).get().await()
            snapshot.documents.mapNotNull { doc ->
                val interest = doc.toObject(InterestModel::class.java)
                interest?.copy(id = doc.id, timestamp = formatFirebaseTimestamp(interest.timestamp))
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getAllInterests(): List<InterestModel> {
        return try {
            val snapshot = db.collection("interests").get().await()
            snapshot.documents.mapNotNull { doc ->
                val interest = doc.toObject(InterestModel::class.java)
                interest?.copy(id = doc.id, timestamp = formatFirebaseTimestamp(interest.timestamp))
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getPendingInterests(): List<InterestModel> {
        return try {
            val snapshot =
                db.collection("interests").whereEqualTo("status", "Pendente").get().await()
            snapshot.documents.mapNotNull { doc ->
                val interest = doc.toObject(InterestModel::class.java)
                interest?.copy(id = doc.id, timestamp = formatFirebaseTimestamp(interest.timestamp))
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun confirmInterest(interest: InterestModel): Boolean {
        return try {
            db.runTransaction { transaction ->
                val carRef = db.collection("cars").document(interest.carId)
                val carSnapshot = transaction.get(carRef)
                val currentQuantity = carSnapshot.getLong("quantity")?.toInt() ?: 0
                if (currentQuantity <= 0) {
                    return@runTransaction false
                }

                val currentMonth = SimpleDateFormat("yyyy-MM").format(Date())
                val confirmationDocRef = db.collection("confirmations").document(currentMonth)
                val confirmationSnapshot = transaction.get(confirmationDocRef)

                transaction.update(carRef, "quantity", currentQuantity - 1)
                if (confirmationSnapshot.exists()) {
                    val currentCount = confirmationSnapshot.getLong("count") ?: 0
                    transaction.update(confirmationDocRef, "count", currentCount + 1)
                } else {
                    val data = hashMapOf("count" to 1, "month" to currentMonth)
                    transaction.set(confirmationDocRef, data)
                }
                transaction.update(confirmationDocRef, "carName", interest.carName)
                transaction.update(
                    db.collection("interests").document(interest.id),
                    "status",
                    "Confirmado"
                )

                true
            }.await()
        } catch (_: Exception) {
            false
        }
    }

    suspend fun cancelInterest(interestId: String): Boolean {
        return try {
            db.collection("interests").document(interestId).update("status", "Cancelado").await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun getConfirmationData(): Map<String, Int> {
        return try {
            val snapshot = db.collection("confirmations").get().await()
            snapshot.documents.associate { doc ->
                val month = doc.getString("month") ?: ""
                val count = doc.getLong("count")?.toInt() ?: 0
                month to count
            }
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun formatFirebaseTimestamp(firebaseTimestamp: String): String {
        val dateFormatFirebase = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return try {
            val date = dateFormatFirebase.parse(firebaseTimestamp)
            date?.let { dateFormat.format(it) } ?: "Data Inválida"
        } catch (_: Exception) {
            "Data Inválida"
        }
    }

    suspend fun saveInterest(interest: InterestModel): Boolean {
        return try {
            db.collection("interests").add(interest).await()
            true
        } catch (_: Exception) {
            false
        }
    }
}