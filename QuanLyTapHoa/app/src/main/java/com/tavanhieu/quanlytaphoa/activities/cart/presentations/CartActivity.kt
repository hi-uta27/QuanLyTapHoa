package com.tavanhieu.quanlytaphoa.activities.cart.presentations

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.cart.adapter.CartAdapter
import com.tavanhieu.quanlytaphoa.activities.cart.domain.infra.CartUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case.CartUseCase
import com.tavanhieu.quanlytaphoa.activities.depot.presentations.DepotActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.util.Calendar

class CartActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var createOrderButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyCartTextView: TextView

    private val cartUseCase: CartUseCase by lazy { CartUseCaseImpl() }
    private val adapter: CartAdapter by lazy { CartAdapter() }
    private var carts: ArrayList<Cart> = ArrayList()

    override fun setContentView() {
        setContentView(R.layout.activity_cart)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        recyclerView = findViewById(R.id.recycleView)
        createOrderButton = findViewById(R.id.createOrderButton)
        progressBar = findViewById(R.id.progressBar)
        emptyCartTextView = findViewById(R.id.emptyCartTextView)
    }

    override fun configLayout() {
        refreshCart()

        imageBack.setOnClickListener { finish() }
        adapter.deleteCartWith = { deleteCartWith(it) }
        createOrderButton.setOnClickListener { createOrder() }
    }

    private fun createOrder() {
        if (carts.isEmpty()) {
            showAlertDialog(
                getResourceText(R.string.notification),
                getResourceText(R.string.emptyCart),
                getResourceText(R.string.add)
            ) {
                startActivity(Intent(this, DepotActivity::class.java))
            }
        } else {
            progressBar.visibility = View.VISIBLE
            val calendar = Calendar.getInstance()
            val order = Order(
                calendar.timeInMillis.toString(),
                FirebaseNetworkLayer.instance.requestCurrentUserUID(),
                carts,
                calendar.time
            )

            cartUseCase.createOrderWith(order, {
                createOrderSuccess()
            }, {
                createOrderFailure()
            })
        }
    }

    private fun createOrderFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(
            getResourceText(R.string.error),
            getResourceText(R.string.createOrderFailure),
            getResourceText(R.string.tryAgain)
        ) {
            createOrder()
        }
    }

    private fun createOrderSuccess() {
        progressBar.visibility = View.GONE
        showToast(getResourceText(R.string.createOrderSuccess))
        emptyCartTextView.visibility = View.VISIBLE
    }

    private fun deleteCartWith(idProduct: String) {
        showAlertDialog(
            getResourceText(R.string.notification),
            getResourceText(R.string.deleteCart),
            getResourceText(R.string.confirm)
        ) {
            progressBar.visibility = View.VISIBLE
            cartUseCase.deleteCartWith(idProduct, {
                progressBar.visibility = View.GONE
                showToast(getResourceText(R.string.deleteCartSuccess))
            }, {
                progressBar.visibility = View.GONE
                showToast(getResourceText(R.string.deleteCartFailure))
            })
        }
    }

    private fun refreshCart() {
        progressBar.visibility = View.VISIBLE
        cartUseCase.refreshCart({
            refreshCartSuccess(it)
        }, {
            refreshCartFailure()
        })
    }

    private fun refreshCartFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(
            getResourceText(R.string.error),
            getResourceText(R.string.readCartFailure),
            getResourceText(R.string.tryAgain)
        ) {
            refreshCart()
        }
    }

    private fun refreshCartSuccess(carts: ArrayList<Cart>) {
        progressBar.visibility = View.GONE
        createOrderButton.isEnabled = true
        adapter.setData(carts)
        recyclerView.adapter = adapter
        this.carts = carts

        if (carts.isEmpty()) {
            emptyCartTextView.visibility = View.VISIBLE
        } else {
            emptyCartTextView.visibility = View.GONE
        }
    }
}