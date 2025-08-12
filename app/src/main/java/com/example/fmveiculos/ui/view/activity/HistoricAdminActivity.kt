package com.example.fmveiculos.ui.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.HistoricAdminContract
import com.example.fmveiculos.presenter.impl.HistoricAdminPresenter
import com.example.fmveiculos.ui.view.adapter.HistoricAdapter
import com.example.fmveiculos.utils.Navigator

class HistoricAdminActivity : AppCompatActivity(), HistoricAdminContract.View {

    private lateinit var presenter: HistoricAdminPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoricAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_admin)

        presenter = HistoricAdminPresenter(this, InterestRepository())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        recyclerView = findViewById(R.id.recyclerViewInterests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoricAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.loadInterests()
    }

    override fun displayInterests(interests: List<InterestModel>) {
        adapter.updateInterests(interests)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeAdminActivity::class.java)
    }
}