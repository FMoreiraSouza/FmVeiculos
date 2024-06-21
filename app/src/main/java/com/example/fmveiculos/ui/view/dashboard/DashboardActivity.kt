package com.example.fmveiculos.ui.view.dashboard

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fmveiculos.R
import com.example.fmveiculos.ui.view.home.HomeAdminActivity
import com.example.fmveiculos.utils.Navigator
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAXAxis
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAYAxis
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private val months = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        firestore = FirebaseFirestore.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
        }

    }

    override fun onStart() {
        super.onStart()
        loadDataAndDrawChart(null)
    }

    private fun loadDataAndDrawChart(selectedMonth: String?) {
        firestore.collection("confirmations")
            .get()
            .addOnSuccessListener { result ->
                val counts = mutableListOf<Int>()

                months.clear()
                for (document in result) {
                    val month = document.getString("month") ?: ""
                    val count = document.getLong("count")?.toInt() ?: 0
                    if (month.isNotEmpty()) {
                        months.add(month)
                    }
                    if (selectedMonth == null || month == selectedMonth) {
                        counts.add(count)
                    }
                }

                val sortedData = if (selectedMonth == null) {
                    months.zip(counts).sortedBy { it.first }
                } else {
                    listOf(selectedMonth to counts.sum())
                }

                val sortedMonths = sortedData.map { it.first }
                val sortedCounts = sortedData.map { it.second }

                // Criação do modelo do gráfico
                val aaChartModel: AAChartModel = AAChartModel()
                    .chartType(AAChartType.Area)
                    .title("Interesses Confirmados")
                    .subtitle("Exibição dos dados de interesses confirmados")
                    .backgroundColor("#FFFFFF") // colorPrimary
                    .dataLabelsEnabled(true)
                    .categories(sortedMonths.toTypedArray())
                    .series(
                        arrayOf(
                            AASeriesElement()
                                .name("Interesses Confirmados")
                                .data(sortedCounts.toTypedArray())
                                .color("#FF4500") // metallic_red
                        )
                    )

                // Configurações detalhadas do gráfico
                val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
                aaChartView.aa_drawChartWithChartModel(aaChartModel)

                // Configuração do eixo X
                val aaXAxis = AAXAxis()
                    .gridLineColor("#A9A9A9") // metallic_gray
                    .labels(AALabels().style(AAStyle().color("#000000"))) // colorContrast

                // Configuração do eixo Y
                val aaYAxis = AAYAxis()
                    .gridLineColor("#A9A9A9") // metallic_gray
                    .labels(AALabels().style(AAStyle().color("#000000"))) // colorContrast

                // Configura o gráfico com os eixos configurados
                val aaOptions = aaChartModel.aa_toAAOptions()
                aaOptions.xAxis = aaXAxis
                aaOptions.yAxis = aaYAxis
                aaChartView.aa_refreshChartWithChartOptions(aaOptions)
            }
            .addOnFailureListener { exception ->
                // Trate a falha na obtenção dos dados aqui
            }
    }

    private fun showMonthSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecione o Mês")

        builder.setItems(months.toTypedArray()) { dialog, which ->
            loadDataAndDrawChart(months[which])
        }

        builder.show()
    }
}
