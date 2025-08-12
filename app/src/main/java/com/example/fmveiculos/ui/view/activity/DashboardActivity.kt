package com.example.fmveiculos.ui.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fmveiculos.R
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.DashboardContract
import com.example.fmveiculos.presenter.impl.DashboardPresenter
import com.example.fmveiculos.utils.Navigator
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAXAxis
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAYAxis

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this, InterestRepository())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.loadChartData(null)
    }

    override fun displayChart(months: List<String>, counts: List<Int>) {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Interesses Confirmados")
            .subtitle("Exibição dos dados de interesses confirmados")
            .backgroundColor("#FFFFFF")
            .dataLabelsEnabled(true)
            .categories(months.toTypedArray())
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Interesses Confirmados")
                        .data(counts.toTypedArray())
                        .color("#FF4500")
                )
            )

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaXAxis =
            AAXAxis().gridLineColor("#A9A9A9").labels(AALabels().style(AAStyle().color("#000000")))
        val aaYAxis =
            AAYAxis().gridLineColor("#A9A9A9").labels(AALabels().style(AAStyle().color("#000000")))
        val aaOptions = aaChartModel.aa_toAAOptions()
        aaOptions.xAxis = aaXAxis
        aaOptions.yAxis = aaYAxis
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
        aaChartView.aa_refreshChartWithChartOptions(aaOptions)
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }
}