package com.tavanhieu.quanlytaphoa.commons.base

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

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

    fun <T : TextView> checkNullOrEmptyWithText(view: T, textError: String): Boolean {
        val text = view.text.trim().toString()
        if (text == "" || text.isEmpty()) {
            view.error = textError
            view.requestFocus()
            return false
        }
        return true
    }
}

// MARK: - Alert Dialog
fun BaseActivity.showErrorDialog(title: String, message: String, titlePositiveButton: String,  complete: () -> Unit) {
    AlertDialog.Builder(this).setTitle(title).setMessage(message)
        .setPositiveButton(titlePositiveButton) { _, _ -> complete() }
        .setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
        .show()
}