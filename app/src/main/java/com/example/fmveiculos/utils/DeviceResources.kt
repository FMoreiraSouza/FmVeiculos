package com.example.fmveiculos.utils

import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class DeviceResources : AppCompatActivity() {

    fun openGallery(callback: (Uri?) -> Unit, setUriCallback: (Uri?) -> Unit) {
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                setUriCallback.invoke(uri)
                callback.invoke(uri)
                Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_LONG).show()
            }
        }
    }
}