package com.tavanhieu.quanlytaphoa.activities.order.presentations

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.cart.presentations.CartActivity
import com.tavanhieu.quanlytaphoa.activities.order.adapter.OrderAdapter
import com.tavanhieu.quanlytaphoa.activities.order.domain.infra.OrderUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.order.domain.use_cases.OrderUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Order

class OrderActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var floatingActionButton: ExtendedFloatingActionButton

    private val orderUseCase: OrderUseCase by lazy { OrderUseCaseImpl() }
    private val adapter: OrderAdapter by lazy { OrderAdapter() }

    override fun setContentView() {
        setContentView(R.layout.activity_order)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        emptyTextView = findViewById(R.id.emptyTextView)
        recyclerView = findViewById(R.id.recycleView)
        progressBar = findViewById(R.id.progressBar)
        floatingActionButton = findViewById(R.id.floatingActionButton)
    }

    override fun configLayout() {
        refreshOrder()

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        imageBack.setOnClickListener { finish() }
    }

    private fun refreshOrder() {
        progressBar.visibility = View.VISIBLE
        orderUseCase.refresh({
            refreshDepotSuccess(it)
        }, {
            refreshDepotFailure()
        })
    }

    private fun refreshDepotSuccess(orders: ArrayList<Order>) {
        progressBar.visibility = View.GONE
        adapter.setData(this, orders)
        recyclerView.adapter = adapter

        if(orders.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }

    private fun refreshDepotFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(getResourceText(R.string.error),
            getResourceText(R.string.readDepotFailure),
            getResourceText(R.string.tryAgain)
        ) {
            refreshOrder()
        }
    }
}