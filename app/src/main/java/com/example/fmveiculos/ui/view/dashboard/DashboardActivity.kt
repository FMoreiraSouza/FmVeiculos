package com.example.fmveiculos.ui.view.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fmveiculos.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private var contadorConfirmados: Int = 0 // Variável para armazenar o contador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Inicializa o Firebase Firestore
        firestore = FirebaseFirestore.getInstance()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        // Chama o método para contar os dados com status confirmado
        contarDadosConfirmados()

    }

    private fun contarDadosConfirmados() {
        // Referência para a coleção 'interesses' no Firestore
        val interessesRef = firestore.collection("interesses")

        // Query para pegar apenas os documentos com status 'confirmado'
        interessesRef.whereEqualTo("status", "Confirmado")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Inicializa o contador
                var contador = 0

                // Itera sobre os documentos encontrados
                for (document in querySnapshot.documents) {
                    // Verifica se o status é 'Confirmado'
                    val status = document.getString("status")
                    if (status == "Confirmado") {
                        // Incrementa o contador para cada documento com status 'Confirmado'
                        contador++
                    }
                }

                // Atribui o valor do contador à variável de classe ou global
                contadorConfirmados = contador

                // Exibe ou utiliza o contador conforme necessário
                println("Número de interesses confirmados: $contadorConfirmados")

                // Aqui você pode chamar métodos ou fazer outras operações baseadas no contador
                // por exemplo, atualizar a interface do usuário com o contador.
            }
            .addOnFailureListener { exception ->
                // Tratar falhas, se houver
                println("Erro ao contar interesses confirmados: $exception")
            }
    }

}
