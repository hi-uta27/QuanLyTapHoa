package com.tavanhieu.quanlytaphoa.activities.detail_product.presentations

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.formatCurrency
import com.tavanhieu.quanlytaphoa.commons.models.Product
import java.text.ParsePosition
import java.text.SimpleDateFormat

class DetailProductActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
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

    override fun setContentView() {
        setContentView(R.layout.activity_detail_product)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
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

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun configLayout() {
        val product = intent.getSerializableExtra("Product") as Product
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        nameTextView.text = product.name
        entryDateTextView.text = "${getResourceText(R.string.entryDate)}: ${simpleDateFormat.format(product.entryDate)}"
        quantityTextView.text = "${getResourceText(R.string.remaining)}: ${product.quantity} ${product.type}" +
                " - ${getResourceText(R.string.expiredDate)}: ${simpleDateFormat.format(product.expiredDate)}"
        originalPriceTextView.text = "${getResourceText(R.string.originalPrice)}: ${product.originalPrice.formatCurrency()}"
        priceTextView.text = "${getResourceText(R.string.price)}: ${product.price.formatCurrency()}"
        if (product.description == "") {
            descriptionTextView.text = getResourceText(R.string.noDescription)
        } else {
            descriptionTextView.text = product.description
        }
        if (product.image != null) {
            Picasso.get().load(product.image).into(productImageView)
        }
        //
        imageBack.setOnClickListener { finish() }
    }
}