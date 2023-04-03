package com.tavanhieu.quanlytaphoa.activities.detail_product.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra.DetailProductUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases.DetailProductUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Product
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpdateProductActivity : BaseActivity() {
    private lateinit var productImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var nameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var expiredDateTextView: TextView
    private lateinit var calendarImageButton: ImageButton
    private lateinit var originalPriceEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var imageBack: ImageView

    private var expiredCalendar: Calendar = Calendar.getInstance()
    private val detailProductUseCase: DetailProductUseCase by lazy { DetailProductUseCaseImpl() }
    private var uriImageGallery: Uri? = null
    private lateinit var product: Product
    private var arrSpinner = ArrayList<String>()

    override fun setContentView() {
        setContentView(R.layout.activity_update_product)
    }

    override fun mappingViewId() {
        productImageView = findViewById(R.id.productImageView)
        progressBar = findViewById(R.id.progressBar)
        nameEditText = findViewById(R.id.nameEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        expiredDateTextView = findViewById(R.id.expiredDateTextView)
        calendarImageButton = findViewById(R.id.calenderImageButton)
        originalPriceEditText = findViewById(R.id.originalPriceEditText)
        priceEditText = findViewById(R.id.priceEditText)
        typeSpinner = findViewById(R.id.typeSpinner)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        updateButton = findViewById(R.id.updateButton)
        imageBack = findViewById(R.id.imageBack)
    }

    @SuppressLint("SimpleDateFormat")
    override fun configLayout() {
        product = intent.getSerializableExtra("UpdateProduct") as Product
        loadProductData(product)

        imageBack.setOnClickListener { finish() }
        updateButton.setOnClickListener { updateProduct() }
        calendarImageButton.setOnClickListener { openDatePicker() }
        productImageView.setOnClickListener { activityResultCamera.launch("image/*") }

        arrSpinner.add("Chai")
        arrSpinner.add("Gói")
        arrSpinner.add("Hộp")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrSpinner)
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadProductData(product: Product) {
        Picasso.get().load(product.image).placeholder(R.drawable.ic_photo).into(productImageView)
        nameEditText.setText(product.name)
        quantityEditText.setText("${product.quantity}")
        originalPriceEditText.setText(product.originalPrice.toLong().toString())
        priceEditText.setText(product.price.toLong().toString())
        descriptionEditText.setText(product.description)
        expiredDateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(product.expiredDate)
        expiredCalendar.time = product.expiredDate
    }

    private var activityResultCamera = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            uriImageGallery = it
            productImageView.setImageURI(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun openDatePicker() {
        DatePickerDialog(
            this,
            { _, i, i2, i3 ->
                expiredCalendar.set(Calendar.YEAR, i)
                expiredCalendar.set(Calendar.MONTH, i2)
                expiredCalendar.set(Calendar.DAY_OF_MONTH, i3)
                expiredDateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(expiredCalendar.time)
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateProduct() {
        if (checkNullOrEmptyWithText(nameEditText)) {
            showErrorWithEditText(nameEditText, getResourceText(R.string.noNameProduct))
        } else if (checkNullOrEmptyWithText(quantityEditText)) {
            showErrorWithEditText(quantityEditText, getResourceText(R.string.noQuantity))
        } else if (checkNullOrEmptyWithText(originalPriceEditText)) {
            showErrorWithEditText(originalPriceEditText, getResourceText(R.string.noOriginalPrice))
        } else if (checkNullOrEmptyWithText(priceEditText)) {
            showErrorWithEditText(priceEditText, getResourceText(R.string.noPrice))
        } else if(originalPriceEditText.text.toString().toFloat() > priceEditText.text.toString().toFloat()) {
            showErrorWithEditText(priceEditText, getResourceText(R.string.checkPrice))
        } else {
            val name = nameEditText.text.trim().toString()
            val quantity = quantityEditText.text.trim().toString().toInt()
            val originalPrice = originalPriceEditText.text.trim().toString().toFloat()
            val price = priceEditText.text.trim().toString().toFloat()
            val description = descriptionEditText.text.trim().toString()
            val expiredDate = expiredCalendar.time
            val type = typeSpinner.selectedItem.toString()

            val result = Product(product.id, name, product.image, description, type, product.entryDate, expiredDate, quantity, originalPrice, price)
            progressBar.visibility = View.VISIBLE
            enableView(false)
            detailProductUseCase.updateProduct(result, uriImageGallery, {
                updateProductSuccess()
            }, {
                updateProductFailure()
            })
        }
    }

    private fun updateProductSuccess() {
        showToast(getResourceText(R.string.updateProductSuccess))
        finish()
    }

    private fun updateProductFailure() {
        progressBar.visibility = View.GONE
        enableView(true)
        showAlertDialog(
            getResourceText(R.string.cancel),
            getResourceText(R.string.updateProductFailure),
            getResourceText(R.string.tryAgain)) {
            updateProduct()
        }
    }

    private fun enableView(enable: Boolean) {
        expiredDateTextView.isEnabled = enable
        productImageView.isEnabled = enable
        calendarImageButton.isEnabled = enable
        nameEditText.isEnabled = enable
        quantityEditText.isEnabled = enable
        originalPriceEditText.isEnabled = enable
        priceEditText.isEnabled = enable
        descriptionEditText.isEnabled = enable
        typeSpinner.isEnabled = enable
        updateButton.isEnabled = enable
    }
}