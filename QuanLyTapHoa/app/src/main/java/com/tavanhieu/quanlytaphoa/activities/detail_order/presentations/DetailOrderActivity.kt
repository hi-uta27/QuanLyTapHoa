package com.tavanhieu.quanlytaphoa.activities.detail_order.presentations

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.detail_order.adapter.DetailOrderAdapter
import com.tavanhieu.quanlytaphoa.activities.detail_order.domain.infra.DetailOrderUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.detail_order.domain.use_case.DetailOrderUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Order
import java.text.SimpleDateFormat

class DetailOrderActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var idTextView: TextView
    private lateinit var employeeCreationTextView: TextView
    private lateinit var creationDateTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val detailOrderUseCase: DetailOrderUseCase by lazy { DetailOrderUseCaseImpl() }
    private val adapter: DetailOrderAdapter by lazy { DetailOrderAdapter() }
    private var idOrder: String? = null

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
        idOrder = intent.getStringExtra("IdOrder")
        idOrder?.let { readDetailOrderWith(it) }

        imageBack.setOnClickListener { finish() }
    }

    private fun readDetailOrderWith(idOrder: String) {
        progressBar.visibility = View.VISIBLE
        detailOrderUseCase.readDetailOrderWith(idOrder, { order ->
            handleReadDetailOrderSuccess(order)
        }, {
            handleReadDetailOrderFailure()
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun handleReadDetailOrderSuccess(order: Order) {
        progressBar.visibility = View.GONE
        idTextView.text = order.id
        creationDateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(order.date)

        var totalPrice = 0F
        order.carts.forEach {
            totalPrice += it.product.price * it.quantity
        }
        totalPriceTextView.text = totalPrice.formatCurrency()

        detailOrderUseCase.readEmployeeWith(order, { employeeCreationTextView.text = it.name }, {})
        adapter.setData(this, order.carts)
        recyclerView.adapter = adapter
    }

    private fun handleReadDetailOrderFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(
            getResourceText(R.string.error),
            getResourceText(R.string.readOrderFailure),
            getResourceText(R.string.tryAgain)
        ) {
            idOrder?.let { readDetailOrderWith(it) }
        }
    }
}