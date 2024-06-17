package com.example.fmveiculos.viewModel.restocking

import CarModel
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class RestockingViewModel : ViewModel() {

    var uriSelectedImage: Uri? = null
    var bitmapSelectedImage: Bitmap? = null

    fun setSelectedImage(uri: Uri?) {
        uriSelectedImage = uri
    }

    fun setBitmapImage(bitmap: Bitmap?) {
        bitmapSelectedImage = bitmap
    }

    fun uploadImageAndSaveCar(
        car: CarModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val storageReference = FirebaseStorage.getInstance().reference
        val fileReference = storageReference.child("car_images/${System.currentTimeMillis()}.jpg")

        if (uriSelectedImage != null) {
            fileReference.putFile(uriSelectedImage!!)
                .addOnSuccessListener { taskSnapshot ->
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        car.imageResource = imageUrl
                        saveCarToFirestore(car, onSuccess, onFailure)
                    }
                }
                .addOnFailureListener {
                    onFailure("Falha ao fazer upload da imagem")
                }
        } else if (bitmapSelectedImage != null) {
            val baos = ByteArrayOutputStream()
            bitmapSelectedImage!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            fileReference.putBytes(data)
                .addOnSuccessListener { taskSnapshot ->
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        car.imageResource = imageUrl
                        saveCarToFirestore(car, onSuccess, onFailure)
                    }
                }
                .addOnFailureListener {
                    onFailure("Falha ao fazer upload da imagem")
                }
        }
    }

    private fun saveCarToFirestore(
        car: CarModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("cars")
            .add(car)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure("Falha ao salvar carro")
            }
    }
}
