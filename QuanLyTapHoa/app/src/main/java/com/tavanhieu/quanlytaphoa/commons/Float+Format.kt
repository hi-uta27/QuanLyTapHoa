package com.tavanhieu.quanlytaphoa.commons

import java.text.NumberFormat
import java.util.*

fun Float.formatCurrency(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("VND")
    return format.format(this.toLong())
}