package com.tavanhieu.quanlytaphoa.activities.cart.presentations

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.cart.adapter.CartAdapter
import com.tavanhieu.quanlytaphoa.activities.cart.domain.infra.CartUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case.CartUseCase
import com.tavanhieu.quanlytaphoa.activities.depot.presentations.DepotActivity
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra.DetailProductUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases.DetailProductUseCase
import com.tavanhieu.quanlytaphoa.activities.search.domain.infra.SearchUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.search.domain.use_cases.SearchUseCase
import com.tavanhieu.quanlytaphoa.commons.CaptureAct
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.util.Calendar

class CartActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var qrCodeImageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var createOrderButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyCartTextView: TextView
    private lateinit var totalPriceTextView: TextView

    private val cartUseCase: CartUseCase by lazy { CartUseCaseImpl() }
    private val searchUseCase: SearchUseCase by lazy { SearchUseCaseImpl() }
    private val detailProductUseCase: DetailProductUseCase by lazy { DetailProductUseCaseImpl() }
    private val adapter: CartAdapter by lazy { CartAdapter() }
    private var carts: ArrayList<Cart> = ArrayList()

    override fun setContentView() {
        setContentView(R.layout.activity_cart)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        qrCodeImageView = findViewById(R.id.qrCodeImageView)
        recyclerView = findViewById(R.id.recycleView)
        createOrderButton = findViewById(R.id.createOrderButton)
        progressBar = findViewById(R.id.progressBar)
        emptyCartTextView = findViewById(R.id.emptyCartTextView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
    }

    override fun configLayout() {
        refreshCart()

        imageBack.setOnClickListener { finish() }
        qrCodeImageView.setOnClickListener { openScanBarCode() }
        adapter.deleteCartWith = { deleteCartWith(it) }
        adapter.minusQuantity = { minusQuantity(it) }
        adapter.plusQuantity = { plusQuantity(it) }
        createOrderButton.setOnClickListener { createOrder() }
    }

    // --------------------------------------------------------------------------------------

    private var activityResult = registerForActivityResult(ScanContract()) {
        if (it.contents != null) {
            searchProductById(it.contents)
        }
    }

    private fun openScanBarCode() {
        val scanOptions = ScanOptions()
        scanOptions.setPrompt(getResourceText(R.string.idProduct))
        scanOptions.setBeepEnabled(true) //mở chuông khi quét được barCode
        scanOptions.setCameraId(0) //camera sau
        //Yêu cầu quyền đọc camera
        scanOptions.captureActivity = CaptureAct::class.java
        //Lắng nghe dữ liệu trả về
        activityResult.launch(scanOptions)
    }

    private fun searchProductById(id: String) {
        progressBar.visibility = View.VISIBLE
        searchUseCase.searchProductById(id, {
            searchProductByIdSuccess(it)
        }, {
            searchProductByIdFailure()
        })
    }

    private fun searchProductByIdSuccess(product: Product?) {
        progressBar.visibility = View.GONE
        if (product == null) {
            showToast(getResourceText(R.string.noProduct))
        } else {
            showAlertDialog(
                getResourceText(R.string.notification),
                getResourceText(R.string.addToCart),
                getResourceText(R.string.add)
            ) {
                val cart = Cart(product, 1)
                detailProductUseCase.addProductToCartWith(cart, {
                    showToast(getResourceText(R.string.addToCartSuccess))
                }, {
                    showToast(getResourceText(R.string.addToCartFailure))
                })
            }
        }
    }

    private fun searchProductByIdFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(
            getResourceText(R.string.error),
            getResourceText(R.string.searchFailure),
            getResourceText(R.string.cancel)
        ) {}
    }

    // --------------------------------------------------------------------------------------

    private fun plusQuantity(cart: Cart) {
        if (cart.quantity < cart.product.quantity) {
            cartUseCase.updateQuantity(cart.quantity + 1, cart.product.id, {}, {})
        } else {
            showToast(getResourceText(R.string.quantityNotEnough))
        }
    }

    private fun minusQuantity(cart: Cart) {
        if (cart.quantity > 1) {
            cartUseCase.updateQuantity(cart.quantity - 1, cart.product.id, {}, {})
        }
    }

    // --------------------------------------------------------------------------------------

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

            cartUseCase.createOrderWith(order, carts, {
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
        emptyCartTextView.visibility = View.VISIBLE
        showToast(getResourceText(R.string.createOrderSuccess))
    }

    // --------------------------------------------------------------------------------------

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

    // --------------------------------------------------------------------------------------

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

    @SuppressLint("SetTextI18n")
    private fun refreshCartSuccess(carts: ArrayList<Cart>) {
        progressBar.visibility = View.GONE
        createOrderButton.isEnabled = true
        adapter.setData(this, carts)
        recyclerView.adapter = adapter
        this.carts = carts

        var totalPrice = 0F
        if (carts.isEmpty()) {
            emptyCartTextView.visibility = View.VISIBLE
        } else {
            carts.forEach {
                totalPrice += it.product.price * it.quantity
            }
            emptyCartTextView.visibility = View.GONE
        }
        totalPriceTextView.text = "${getResourceText(R.string.totalPrice)}: ${totalPrice.formatCurrency()}"
    }
}