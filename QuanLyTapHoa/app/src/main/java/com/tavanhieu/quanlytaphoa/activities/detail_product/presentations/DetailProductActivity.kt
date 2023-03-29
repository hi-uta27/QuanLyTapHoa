package com.tavanhieu.quanlytaphoa.activities.detail_product.presentations

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra.DetailProductUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases.DetailProductUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import java.text.SimpleDateFormat

class DetailProductActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var productImageView: ImageView
    private lateinit var minusImageView: ImageView
    private lateinit var plusImageView: ImageView
    private lateinit var entryDateTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var quantityTextView: TextView
    private lateinit var originalPriceTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var buyQuantityTextView: TextView
    private lateinit var updateProductImageButton: ImageButton
    private lateinit var deleteProductImageButton: ImageButton
    private lateinit var addToOrderButton: Button

    private val detailProductUseCase: DetailProductUseCase by lazy { DetailProductUseCaseImpl() }

    override fun setContentView() {
        setContentView(R.layout.activity_detail_product)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        progressBar = findViewById(R.id.progressBar)
        productImageView = findViewById(R.id.productImageView)
        minusImageView = findViewById(R.id.minusImageView)
        plusImageView = findViewById(R.id.plusImageView)
        entryDateTextView = findViewById(R.id.entryDateTextView)
        nameTextView = findViewById(R.id.nameTextView)
        quantityTextView = findViewById(R.id.quantityTextView)
        originalPriceTextView = findViewById(R.id.originalPriceTextView)
        priceTextView = findViewById(R.id.priceTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        buyQuantityTextView = findViewById(R.id.buyQuantityTextView)
        updateProductImageButton = findViewById(R.id.updateImageButton)
        deleteProductImageButton = findViewById(R.id.deleteImageButton)
        addToOrderButton = findViewById(R.id.addToOrderButton)
    }

    override fun configLayout() {
        val idProduct = intent.getStringExtra("IdProduct") as String
        readProductWith(idProduct)
        imageBack.setOnClickListener { finish() }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun readProductWith(idProduct: String) {
        progressBar.visibility = View.VISIBLE
        detailProductUseCase.readProductWith(idProduct, {
            progressBar.visibility = View.GONE
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            nameTextView.text = it.name
            entryDateTextView.text = "${getResourceText(R.string.entryDate)}: ${simpleDateFormat.format(it.entryDate)}"
            quantityTextView.text = "${getResourceText(R.string.remaining)}: ${it.quantity} ${it.type}" +
                    " - ${getResourceText(R.string.expiredDate)}: ${simpleDateFormat.format(it.expiredDate)}"
            originalPriceTextView.text = "${getResourceText(R.string.originalPrice)}: ${it.originalPrice.formatCurrency()}"
            priceTextView.text = "${getResourceText(R.string.price)}: ${it.price.formatCurrency()}"
            if (it.description == "") {
                descriptionTextView.text = getResourceText(R.string.noDescription)
            } else {
                descriptionTextView.text = it.description
            }
            if (it.image != null) {
                Picasso.get().load(it.image).into(productImageView)
            }
        }, {
            showAlertDialog(
                getResourceText(R.string.error),
                getResourceText(R.string.readProductFailure),
                getResourceText(R.string.tryAgain)) {
                readProductWith((idProduct))
            }
        })

    }
}