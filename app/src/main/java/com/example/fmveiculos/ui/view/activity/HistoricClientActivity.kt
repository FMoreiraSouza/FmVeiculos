package com.example.fmveiculos.ui.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fmveiculos.R
import com.example.fmveiculos.data.model.InterestModel
import com.example.fmveiculos.data.repository.AuthRepository
import com.example.fmveiculos.data.repository.InterestRepository
import com.example.fmveiculos.presenter.contract.HistoricClientContract
import com.example.fmveiculos.presenter.impl.HistoricClientPresenter
import com.example.fmveiculos.ui.view.adapter.HistoricAdapter
import com.example.fmveiculos.utils.Navigator

class HistoricClientActivity : AppCompatActivity(), HistoricClientContract.View {

    private lateinit var presenter: HistoricClientPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoricAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_client)

        presenter = HistoricClientPresenter(this, InterestRepository())

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
        val userId = AuthRepository().getCurrentUser()?.uid
        presenter.loadInterests(userId)
    }

    override fun displayInterests(interests: List<InterestModel>) {
        adapter.updateInterests(interests)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        Navigator().navigateToActivity(this, HomeClientActivity::class.java)
    }
}