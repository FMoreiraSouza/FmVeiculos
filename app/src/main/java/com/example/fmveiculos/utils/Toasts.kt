package com.example.fmveiculos.utils

import android.content.Context
import android.widget.Toast
fun exibirMensagem(context: Context , texto: String) {
    Toast.makeText(context, texto, Toast.LENGTH_LONG).show()
}