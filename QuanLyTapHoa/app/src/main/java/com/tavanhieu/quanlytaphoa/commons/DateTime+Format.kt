package com.tavanhieu.quanlytaphoa.commons

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun Date.formatDate(): String {
    return SimpleDateFormat("dd/MM/yyyy").format(this)
}

fun Date.compareDate(date: Date): Boolean {
    return this.formatDate() == date.formatDate()
}