package com.example.fmveiculos.data.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.fmveiculos.data.model.CarModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class CarRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun getCars(): List<CarModel> {
        Log.d("CarDetailsDebug", "getCars: Iniciando busca de todos os carros")
        return try {
            val snapshot = db.collection("cars").get().await()
            val cars = snapshot.documents.mapNotNull { it.toObject(CarModel::class.java)?.apply { id = it.id } }
            Log.d("CarDetailsDebug", "getCars: ${cars.size} carros encontrados")
            cars
        } catch (e: Exception) {
            Log.e("CarDetailsDebug", "getCars: Erro ao buscar carros: ${e.message}")
            emptyList()
        }
    }

    suspend fun getCarByName(name: String): CarModel? {
        Log.d("CarDetailsDebug", "getCarByName: Iniciando busca para name: $name")
        return try {
            val snapshot = db.collection("cars").whereEqualTo("name", name).get().await()
            if (snapshot.documents.isNotEmpty()) {
                val car = snapshot.documents[0].toObject(CarModel::class.java)?.apply {
                    id = snapshot.documents[0].id
                }
                Log.d("CarDetailsDebug", "getCarByName: Carro encontrado - name: ${car?.name}, id: ${car?.id}")
                car
            } else {
                Log.d("CarDetailsDebug", "getCarByName: Nenhum carro encontrado para name: $name")
                null
            }
        } catch (e: Exception) {
            Log.e("CarDetailsDebug", "getCarByName: Erro ao buscar carro: ${e.message}")
            null
        }
    }

    suspend fun updateCarQuantity(carId: String, quantity: Int): Boolean {
        Log.d("CarDetailsDebug", "updateCarQuantity: Iniciando atualização para carId: $carId, quantity: $quantity")
        return try {
            db.collection("cars").document(carId).update("quantity", quantity).await()
            Log.d("CarDetailsDebug", "updateCarQuantity: Atualização bem-sucedida para carId: $carId")
            true
        } catch (e: Exception) {
            Log.e("CarDetailsDebug", "updateCarQuantity: Erro ao atualizar quantidade: ${e.message}")
            false
        }
    }

    suspend fun updateCarPrice(carId: String, price: Double): Boolean {
        Log.d("CarDetailsDebug", "updateCarPrice: Iniciando atualização para carId: $carId, price: $price")
        return try {
            db.collection("cars").document(carId).update("price", price).await()
            Log.d("CarDetailsDebug", "updateCarPrice: Atualização bem-sucedida para carId: $carId")
            true
        } catch (e: Exception) {
            Log.e("CarDetailsDebug", "updateCarPrice: Erro ao atualizar preço: ${e.message}")
            false
        }
    }

    suspend fun uploadImageAndSaveCar(car: CarModel, uri: Uri?, bitmap: Bitmap?): Boolean {
        Log.d("CarDetailsDebug", "uploadImageAndSaveCar: Iniciando upload para carro: ${car.name}")
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
                Log.d("CarDetailsDebug", "uploadImageAndSaveCar: Nenhuma imagem fornecida")
                return false
            }
            car.imageResource = imageUrl
            val documentRef = db.collection("cars").add(car).await()
            documentRef.update("id", documentRef.id).await()
            Log.d("CarDetailsDebug", "uploadImageAndSaveCar: Carro salvo com id: ${documentRef.id}")
            true
        } catch (e: Exception) {
            Log.e("CarDetailsDebug", "uploadImageAndSaveCar: Erro ao salvar carro: ${e.message}")
            false
        }
    }
}