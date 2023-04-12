package com.tavanhieu.quanlytaphoa.commons.base

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tavanhieu.quanlytaphoa.R

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        mappingViewId()
        configLayout()
    }

    open fun setContentView() {}
    open fun mappingViewId() {}
    open fun configLayout() {}

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun <T : TextView> checkNullOrEmptyWithText(view: T): Boolean {
        val text = view.text.trim().toString()
        return text == "" || text.isEmpty()
    }

    fun <T : EditText> showErrorWithEditText(view: T, textError: String) {
        view.error = textError
        view.requestFocus()
    }

    fun getResourceText(id: Int): String {
        return resources.getText(id).toString()
    }
}

// MARK: - Alert Dialog
fun BaseActivity.showAlertDialog(
    title: String,
    message: String,
    titlePositiveButton: String,
    complete: () -> Unit
) {
    AlertDialog.Builder(this, R.style.AlertDialog).setTitle(title).setMessage(message)
        .setPositiveButton(titlePositiveButton) { _, _ -> complete() }
        .setNegativeButton(getResourceText(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
        .show()
}

fun BaseActivity.showAlertDialog(
    title: String,
    message: String,
    failure: () -> Unit
) {
    AlertDialog.Builder(this, R.style.AlertDialog).setTitle(title).setMessage(message)
        .setNegativeButton(getResourceText(R.string.close)) { _, _ -> failure() }
        .show()
}