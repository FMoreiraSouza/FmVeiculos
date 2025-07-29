package com.example.fmveiculos.presenter.contract

interface DashboardContract {
    interface View {
        fun displayChart(months: List<String>, counts: List<Int>)
        fun navigateToHome()
    }

    interface Presenter {
        fun loadChartData(selectedMonth: String?)
    }
}