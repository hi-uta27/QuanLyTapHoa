package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.TypeConverters
import java.util.Date

class Order {
    var id: String = ""
    var idEmployee: String = ""
    var productOrders: ArrayList<ProductOrder> = ArrayList()

    @TypeConverters(DateConverter::class)
    var date: Date = Date()

    constructor()
    constructor(id: String, idEmployee: String, productOrders: ArrayList<ProductOrder>, date: Date) {
        this.id = id
        this.idEmployee = idEmployee
        this.productOrders = productOrders
        this.date = date
    }
}