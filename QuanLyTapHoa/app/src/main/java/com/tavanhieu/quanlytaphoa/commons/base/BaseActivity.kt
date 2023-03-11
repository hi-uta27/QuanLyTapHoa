package com.tavanhieu.quanlytaphoa.commons.base

import android.os.Bundle
import android.os.Message
import android.widget.TextView
import android.widget.Toast
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

    fun <T : TextView> checkNullOrEmptyWith(view: T, textError: String): Boolean {
        val text = view.text.trim().toString()
        if (text == "" || text.isEmpty()) {
            view.error = textError
            view.requestFocus()
            return false
        }
        return true
    }
}