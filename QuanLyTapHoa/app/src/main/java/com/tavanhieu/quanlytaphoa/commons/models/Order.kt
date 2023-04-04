package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.TypeConverters
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
}