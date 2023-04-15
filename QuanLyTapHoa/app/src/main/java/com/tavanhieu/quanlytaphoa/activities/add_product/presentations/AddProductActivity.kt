package com.tavanhieu.quanlytaphoa.activities.add_product.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.squareup.picasso.Picasso
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.add_product.domain.infra.AddProductUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.add_product.domain.use_case.AddProductUseCase
import com.tavanhieu.quanlytaphoa.activities.search.domain.infra.SearchUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.search.domain.use_cases.SearchUseCase
import com.tavanhieu.quanlytaphoa.commons.CaptureAct
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.formatDate
import com.tavanhieu.quanlytaphoa.commons.models.Product
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Choose Image: Camera, Gallery
// TODO: Spinner can add more type for product

class AddProductActivity : BaseActivity() {
    private lateinit var productImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var idTextView: TextView
    private lateinit var qrCodeImageButton: ImageButton
    private lateinit var nameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var expiredDateTextView: TextView
    private lateinit var calendarImageButton: ImageButton
    private lateinit var originalPriceEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var addButton: Button
    private lateinit var imageBack: ImageView

    private val expiredCalendar: Calendar by lazy { Calendar.getInstance() }
    private val addProductUseCase: AddProductUseCase by lazy { AddProductUseCaseImpl() }
    private val searchUseCase: SearchUseCase by lazy { SearchUseCaseImpl() }
    private var uriImageGallery: Uri? = null
    private var product: Product? = null
    private var arrSpinner = ArrayList<String>()

    override fun setContentView() {
        setContentView(R.layout.activity_add_product)
    }

    override fun mappingViewId() {
        productImageView = findViewById(R.id.productImageView)
        progressBar = findViewById(R.id.progressBar)
        idTextView = findViewById(R.id.idTextView)
        qrCodeImageButton = findViewById(R.id.qrCodeImageButton)
        nameEditText = findViewById(R.id.nameEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        expiredDateTextView = findViewById(R.id.expiredDateTextView)
        calendarImageButton = findViewById(R.id.calenderImageButton)
        originalPriceEditText = findViewById(R.id.originalPriceEditText)
        priceEditText = findViewById(R.id.priceEditText)
        typeSpinner = findViewById(R.id.typeSpinner)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        addButton = findViewById(R.id.addButton)
        imageBack = findViewById(R.id.imageBack)
    }

    @SuppressLint("SimpleDateFormat")
    override fun configLayout() {
        expiredDateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(expiredCalendar.time)

        imageBack.setOnClickListener { finish() }
        addButton.setOnClickListener { addProduct() }
        calendarImageButton.setOnClickListener { openDatePicker() }
        qrCodeImageButton.setOnClickListener { openScanBarCode() }
        productImageView.setOnClickListener { activityResultCamera.launch("image/*") }

        arrSpinner.add("Chai")
        arrSpinner.add("Gói")
        arrSpinner.add("Hộp")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrSpinner)
    }

    // -----------------------------------------------------------------------------------

    private var activityResultCamera = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                uriImageGallery = it
                productImageView.setImageURI(it)
            }
        }

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
        enableView(false)
        searchUseCase.searchProductById(id, {
            searchProductByIdSuccess(id, it)
        }, {})
    }

    private fun searchProductByIdSuccess(id: String, product: Product?) {
        progressBar.visibility = View.GONE
        enableView(true)
        if (product == null) {
            deleteInput()
            idTextView.text = id
        } else {
            product.image?.let {
                productImageView.tag = "ImageView"
                Picasso.get().load(it).placeholder(R.drawable.ic_photo).into(productImageView)
            }
            idTextView.text = product.id
            expiredDateTextView.text = product.expiredDate.formatDate()
            nameEditText.setText(product.name)
            quantityEditText.setText("${product.quantity}")
            originalPriceEditText.setText(product.originalPrice.toLong().toString())
            priceEditText.setText(product.price.toLong().toString())
            descriptionEditText.setText(product.description)
            expiredCalendar.time = product.expiredDate
            this.product = product
        }
    }

    // ------------------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------------------

    private fun addProduct() {
        if (uriImageGallery == null  && product == null) {
            showAlertDialog(
                getResourceText(R.string.notification),
                getResourceText(R.string.noImage),
                getResourceText(R.string.addImage)
            ) {
                activityResultCamera.launch("image/*")
            }
        } else if (idTextView.text == getResourceText(R.string.scanCode)) {
            showAlertDialog(
                getResourceText(R.string.notification),
                getResourceText(R.string.noIdProduct),
                getResourceText(R.string.scanCode)
            ) {
                openScanBarCode()
            }
        } else if (checkNullOrEmptyWithText(nameEditText)) {
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
            val id = idTextView.text.trim().toString()
            val name = nameEditText.text.trim().toString()
            val quantity = quantityEditText.text.trim().toString().toInt()
            val originalPrice = originalPriceEditText.text.trim().toString().toFloat()
            val price = priceEditText.text.trim().toString().toFloat()
            val description = descriptionEditText.text.trim().toString()
            val entryDate = Calendar.getInstance().time
            val expiredDate = expiredCalendar.time
            val type = typeSpinner.selectedItem.toString()

            val product = Product(id, name, this.product?.image, description, type, entryDate, expiredDate, quantity, originalPrice, price)
            progressBar.visibility = View.VISIBLE
            enableView(false)
            addProductUseCase.addProduct(product,
                {
                    addProductSuccess(product)
                }, {
                    addProductFailure()
                })
        }
    }

    private fun addProductSuccess(product: Product) {
        progressBar.visibility = View.GONE
        uriImageGallery?.let {
            addProductUseCase.addImageProduct(product, it, {}, {})
        }
        enableView(true)
        deleteInput()
        showToast(getResourceText(R.string.addProductSuccess))
    }

    private fun addProductFailure() {
        progressBar.visibility = View.GONE
        enableView(true)
        showAlertDialog(
            getResourceText(R.string.cancel),
            getResourceText(R.string.addProductFailure),
            getResourceText(R.string.tryAgain)) {
                addProduct()
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun deleteInput() {
        productImageView.setImageResource(R.drawable.ic_photo)
        idTextView.text = getResourceText(R.string.scanCode)
        nameEditText.setText("")
        quantityEditText.setText("")
        originalPriceEditText.setText("")
        priceEditText.setText("")
        descriptionEditText.setText("")
        typeSpinner.setSelection(0)
        expiredDateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        uriImageGallery = null
        product = null
        nameEditText.requestFocus()
    }

    private fun enableView(enable: Boolean) {
        productImageView.isEnabled = enable
        qrCodeImageButton.isEnabled = enable
        calendarImageButton.isEnabled = enable
        nameEditText.isEnabled = enable
        quantityEditText.isEnabled = enable
        originalPriceEditText.isEnabled = enable
        priceEditText.isEnabled = enable
        descriptionEditText.isEnabled = enable
        typeSpinner.isEnabled = enable
        addButton.isEnabled = enable
    }
}