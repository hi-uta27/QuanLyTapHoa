package com.tavanhieu.quanlytaphoa.activities.detail_order.presentations

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.models.Order

class DetailOrderActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var idTextView: TextView
    private lateinit var employeeCreationTextView: TextView
    private lateinit var creationDateTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun setContentView() {
        setContentView(R.layout.activity_detail_order)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        idTextView = findViewById(R.id.idTextView)
        employeeCreationTextView = findViewById(R.id.employeeCreationTextView)
        creationDateTextView = findViewById(R.id.creationDateTextView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun configLayout() {
        val idOrder = intent.getStringExtra("IdOrder")
        idOrder?.let { loadDataWith(it) }

        imageBack.setOnClickListener { finish() }
    }

    private fun loadDataWith(idOrder: String) {
    }
}