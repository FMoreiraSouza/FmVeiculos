package com.example.fmveiculos.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.fmveiculos.data.model.CarModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class CarRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun getCars(): List<CarModel> {
        return try {
            val snapshot = db.collection("cars").get().await()
            snapshot.documents.mapNotNull { it.toObject(CarModel::class.java) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getCarByName(name: String): CarModel? {
        return try {
            val snapshot = db.collection("cars").whereEqualTo("name", name).get().await()
            if (snapshot.documents.isNotEmpty()) {
                snapshot.documents[0].toObject(CarModel::class.java)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updateCarQuantity(carId: String, quantity: Int): Boolean {
        return try {
            db.collection("cars").document(carId).update("quantity", quantity).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun updateCarPrice(carId: String, price: Double): Boolean {
        return try {
            db.collection("cars").document(carId).update("price", price).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun uploadImageAndSaveCar(car: CarModel, uri: Uri?, bitmap: Bitmap?): Boolean {
        return try {
            val fileReference = storage.reference.child("car_images/${System.currentTimeMillis()}.jpg")
            val imageUrl = if (uri != null) {
                fileReference.putFile(uri).await()
                fileReference.downloadUrl.await().toString()
            } else if (bitmap != null) {
                val byte = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte)
                fileReference.putBytes(byte.toByteArray()).await()
                fileReference.downloadUrl.await().toString()
            } else {
                return false
            }
            car.imageResource = imageUrl
            val documentRef = db.collection("cars").add(car).await()
            documentRef.update("id", documentRef.id).await()
            true
        } catch (_: Exception) {
            false
        }
    }
}