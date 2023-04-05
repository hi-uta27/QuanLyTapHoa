package com.tavanhieu.quanlytaphoa.commons.models

import android.annotation.SuppressLint
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.util.Date

class Order: java.io.Serializable {
    var id: String = ""
    var idEmployee: String = ""
    var carts: ArrayList<Cart> = ArrayList()

    @TypeConverters(DateConverter::class)
    var date: Date = Date()

    constructor()
    constructor(id: String, idEmployee: String, carts: ArrayList<Cart>, date: Date) {
        this.id = id
        this.idEmployee = idEmployee
        this.carts = carts
        this.date = date
    }

    fun totalPrice(): Float {
        var totalPrice = 0F
        carts.forEach {
            totalPrice += it.product.price * it.quantity
        }
        return totalPrice
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToString(): String {
        return SimpleDateFormat("dd/MM/yyyy").format(date)
    }
}