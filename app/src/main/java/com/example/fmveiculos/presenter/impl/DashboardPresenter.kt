package com.example.fmveiculos.presenter.impl

import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.DashboardContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val interestRepository: InterestRepository
) : DashboardContract.Presenter {

    override fun loadChartData(selectedMonth: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            val data: Map<String, Int> = interestRepository.getConfirmationData()
            val months: List<String>
            val counts: List<Int>

            if (selectedMonth == null) {
                months = data.keys.sorted()
                counts = months.map { data[it] ?: 0 }
            } else {
                months = listOf(selectedMonth)
                counts = listOf(data[selectedMonth] ?: 0)
            }
            if (months.isEmpty()) {
                view.displayChart(emptyList(), emptyList())
            } else {
                view.displayChart(months, counts)
            }
        }
    }
}