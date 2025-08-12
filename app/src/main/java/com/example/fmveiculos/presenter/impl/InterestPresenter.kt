package com.example.fmveiculos.presenter.impl

import android.util.Log
import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.InterestContract
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class InterestPresenter(
    private val view: InterestContract.View,
    private val repository: InterestRepository
) : InterestContract.Presenter {

    private val firestore = FirebaseFirestore.getInstance()

    override fun loadPendingInterests() {
        Log.d("InterestPresenter", "loadPendingInterests: Carregando interesses pendentes")
        firestore.collection("interests")
            .whereEqualTo("status", "Pendente")
            .get()
            .addOnSuccessListener { documents ->
                val interests = documents.map { it.toObject(InterestModel::class.java) }
                Log.d("InterestPresenter", "loadPendingInterests: ${interests.size} interesses carregados")
                view.displayInterests(interests)
            }
            .addOnFailureListener { e ->
                Log.e("InterestPresenter", "loadPendingInterests: Erro ao carregar interesses", e)
                view.showError("Erro ao carregar interesses")
            }
    }

    override fun confirmInterest(interest: InterestModel) {
        Log.d("InterestPresenter", "confirmInterest: Confirmando interesse ${interest.id}")
        firestore.collection("cars").document(interest.carId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val currentQuantity = documentSnapshot.getLong("quantity") ?: 0
                if (currentQuantity > 0) {
                    val newQuantity = currentQuantity - 1
                    firestore.collection("cars").document(interest.carId)
                        .update("quantity", newQuantity)
                        .addOnSuccessListener {
                            Log.d("InterestPresenter", "confirmInterest: Quantidade decrementada com sucesso")
                            updateInterestStatusAndConfirmation(interest)
                        }
                        .addOnFailureListener { e ->
                            Log.e("InterestPresenter", "confirmInterest: Erro ao decrementar quantidade", e)
                            view.showError("Erro ao decrementar quantidade")
                        }
                } else {
                    Log.d("InterestPresenter", "confirmInterest: Quantidade insuficiente")
                    view.showError("Quantidade insuficiente do carro")
                }
            }
            .addOnFailureListener { e ->
                Log.e("InterestPresenter", "confirmInterest: Erro ao obter quantidade do carro", e)
                view.showError("Erro ao obter quantidade do carro")
            }
    }

    private fun updateInterestStatusAndConfirmation(interest: InterestModel) {
        val currentMonth = SimpleDateFormat("yyyy-MM").format(Date())
        firestore.runTransaction { transaction ->
            val confirmationDocRef = firestore.collection("confirmations").document(currentMonth)
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
            transaction.update(confirmationDocRef, "carName", interest.carName)
            transaction.update(
                firestore.collection("interests").document(interest.id),
                "status",
                "Confirmado"
            )
        }.addOnSuccessListener {
            Log.d("InterestPresenter", "updateInterestStatusAndConfirmation: Interesse confirmado com sucesso")
            view.showConfirmSuccess()
        }.addOnFailureListener { e ->
            Log.e("InterestPresenter", "updateInterestStatusAndConfirmation: Erro ao confirmar interesse", e)
            view.showError("Erro ao confirmar interesse")
        }
    }

    override fun cancelInterest(interestId: String) {
        Log.d("InterestPresenter", "cancelInterest: Cancelando interesse $interestId")
        firestore.collection("interests").document(interestId)
            .update("status", "Cancelado")
            .addOnSuccessListener {
                Log.d("InterestPresenter", "cancelInterest: Interesse cancelado com sucesso")
                view.showCancelSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("InterestPresenter", "cancelInterest: Erro ao cancelar interesse", e)
                view.showError("Erro ao cancelar interesse")
            }
    }
}