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
        Log.d("InterestRepository", "getInterestsByUser: Buscando interesses para userId: $userId")
        return try {
            val snapshot = db.collection("interests").whereEqualTo("userId", userId).get().await()
            snapshot.documents.mapNotNull { doc ->
                val interest = doc.toObject(InterestModel::class.java)
                interest?.copy(id = doc.id, timestamp = formatFirebaseTimestamp(interest.timestamp))
            }
        } catch (e: Exception) {
            Log.e("InterestRepository", "getInterestsByUser: Erro ao buscar interesses - ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllInterests(): List<InterestModel> {
        Log.d("InterestRepository", "getAllInterests: Buscando todos os interesses")
        return try {
            val snapshot = db.collection("interests").get().await()
            snapshot.documents.mapNotNull { doc ->
                val interest = doc.toObject(InterestModel::class.java)
                interest?.copy(id = doc.id, timestamp = formatFirebaseTimestamp(interest.timestamp))
            }
        } catch (e: Exception) {
            Log.e("InterestRepository", "getAllInterests: Erro ao buscar interesses - ${e.message}")
            emptyList()
        }
    }

    suspend fun getPendingInterests(): List<InterestModel> {
        Log.d("InterestRepository", "getPendingInterests: Buscando interesses pendentes")
        return try {
            val snapshot = db.collection("interests").whereEqualTo("status", "Pendente").get().await()
            snapshot.documents.mapNotNull { doc ->
                val interest = doc.toObject(InterestModel::class.java)
                interest?.copy(id = doc.id, timestamp = formatFirebaseTimestamp(interest.timestamp))
            }
        } catch (e: Exception) {
            Log.e("InterestRepository", "getPendingInterests: Erro ao buscar interesses - ${e.message}")
            emptyList()
        }
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun confirmInterest(interest: InterestModel): Boolean {
        Log.d("InterestRepository", "confirmInterest: Confirmando interesse ${interest.id}")
        return try {
            val carSnapshot = db.collection("cars").document(interest.carId).get().await()
            val currentQuantity = carSnapshot.getLong("quantity")?.toInt() ?: 0
            if (currentQuantity <= 0) {
                Log.d("InterestRepository", "confirmInterest: Quantidade insuficiente para interesse ${interest.id}")
                return false
            }

            db.runTransaction { transaction ->
                transaction.update(db.collection("cars").document(interest.carId), "quantity", currentQuantity - 1)
                val currentMonth = SimpleDateFormat("yyyy-MM").format(Date())
                val confirmationDocRef = db.collection("confirmations").document(currentMonth)
                val snapshot = transaction.get(confirmationDocRef)
                if (snapshot.exists()) {
                    val currentCount = snapshot.getLong("count") ?: 0
                    transaction.update(confirmationDocRef, "count", currentCount + 1)
                } else {
                    val data = hashMapOf("count" to 1, "month" to currentMonth)
                    transaction.set(confirmationDocRef, data)
                }
                transaction.update(confirmationDocRef, "carName", interest.carName)
                transaction.update(db.collection("interests").document(interest.id), "status", "Confirmado")
            }.await()
            Log.d("InterestRepository", "confirmInterest: Interesse ${interest.id} confirmado com sucesso")
            true
        } catch (e: Exception) {
            Log.e("InterestRepository", "confirmInterest: Erro ao confirmar interesse ${interest.id} - ${e.message}")
            false
        }
    }

    suspend fun cancelInterest(interestId: String): Boolean {
        Log.d("InterestRepository", "cancelInterest: Cancelando interesse $interestId")
        return try {
            db.collection("interests").document(interestId).update("status", "Cancelado").await()
            Log.d("InterestRepository", "cancelInterest: Interesse $interestId cancelado com sucesso")
            true
        } catch (e: Exception) {
            Log.e("InterestRepository", "cancelInterest: Erro ao cancelar interesse $interestId - ${e.message}")
            false
        }
    }

    suspend fun saveInterest(interest: InterestModel): Boolean {
        Log.d("InterestRepository", "saveInterest: Salvando interesse ${interest.id}")
        return try {
            val documentRef = db.collection("interests").add(interest).await()
            Log.d("InterestRepository", "saveInterest: Interesse salvo com id: ${documentRef.id}")
            true
        } catch (e: Exception) {
            Log.e("InterestRepository", "saveInterest: Erro ao salvar interesse - ${e.message}")
            false
        }
    }

    suspend fun getConfirmationData(): Map<String, Int> {
        Log.d("InterestRepository", "getConfirmationData: Buscando dados de confirmações")
        return try {
            val snapshot = db.collection("confirmations").get().await()
            snapshot.documents.associate { doc ->
                val month = doc.getString("month") ?: ""
                val count = doc.getLong("count")?.toInt() ?: 0
                month to count
            }
        } catch (e: Exception) {
            Log.e("InterestRepository", "getConfirmationData: Erro ao buscar dados - ${e.message}")
            emptyMap()
        }
    }

    private fun formatFirebaseTimestamp(firebaseTimestamp: String): String {
        val dateFormatFirebase = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return try {
            val date = dateFormatFirebase.parse(firebaseTimestamp)
            date?.let { dateFormat.format(it) } ?: "Data Inválida"
        } catch (e: Exception) {
            Log.e("InterestRepository", "formatFirebaseTimestamp: Erro ao formatar timestamp - ${e.message}")
            "Data Inválida"
        }
    }
}