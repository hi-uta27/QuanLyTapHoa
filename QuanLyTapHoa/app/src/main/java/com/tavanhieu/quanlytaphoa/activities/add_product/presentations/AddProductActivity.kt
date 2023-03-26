package com.tavanhieu.quanlytaphoa.activities.add_product.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import java.text.SimpleDateFormat
import java.util.Calendar

class AddProductActivity : BaseActivity() {
    private lateinit var chooseImageTextView: TextView
    private lateinit var nameImageTextView: TextView
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

    override fun setContentView() {
        setContentView(R.layout.activity_add_product)
    }

    override fun mappingViewId() {
        chooseImageTextView = findViewById(R.id.chooseImageTextView)
        nameImageTextView = findViewById(R.id.nameImageTextView)
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
        showToast(expiredCalendar.time.toString())
    }

    private fun addProduct() {
        if (nameImageTextView.text == getResourceText(R.string.noImage)) {
            showAlertDialog(
                getResourceText(R.string.notification),
                getResourceText(R.string.noImage),
                getResourceText(R.string.addImage)
            ) {
                // open add image
            }
        } else if (idTextView.text == getResourceText(R.string.scanCode)) {
            showAlertDialog(
                getResourceText(R.string.notification),
                getResourceText(R.string.noIdProduct),
                getResourceText(R.string.scanCode)
            ) {
                // open scan qr
            }
        } else if (checkNullOrEmptyWithText(nameEditText)) {
            showErrorWithEditText(nameEditText, getResourceText(R.string.noNameProduct))
        } else if (checkNullOrEmptyWithText(quantityEditText)) {
            showErrorWithEditText(quantityEditText, getResourceText(R.string.noQuantity))
        } else if (checkNullOrEmptyWithText(originalPriceEditText)) {
            showErrorWithEditText(originalPriceEditText, getResourceText(R.string.noOriginalPrice))
        } else if (checkNullOrEmptyWithText(priceEditText)) {
            showErrorWithEditText(priceEditText, getResourceText(R.string.noPrice))
        } else {
            val id = idTextView.text.trim().toString()
            val name = nameEditText.text.trim().toString()
            val quantity = quantityEditText.text.trim().toString()
            val originalPrice = originalPriceEditText.text.trim().toString()
            val price = priceEditText.text.trim().toString()
            val description = descriptionEditText.text.trim().toString()
            val entryDate = Calendar.getInstance().time
            val expiredDate = expiredCalendar.time
            val type = typeSpinner.selectedItem

//            val product = Product(id, name,)
        }
    }
}