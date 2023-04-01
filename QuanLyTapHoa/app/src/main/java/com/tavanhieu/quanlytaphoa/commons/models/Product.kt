package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date
import java.io.Serializable

class Product : Serializable {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var image: String? = null
    var type: String = ""
    var description: String? = null

    @TypeConverters(DateConverter::class)
    var entryDate: Date = Date()

    @TypeConverters(DateConverter::class)
    var expiredDate: Date = Date()
    var quantity: Int = 0
    var originalPrice: Float = 0F
    var price: Float = 0F

    constructor()
    constructor(
        id: String,
        name: String,
        description: String,
        type: String,
        entryDate: Date,
        expiredDate: Date,
        quantity: Int,
        originalPrice: Float,
        price: Float
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.type = type
        this.entryDate = entryDate
        this.expiredDate = expiredDate
        this.quantity = quantity
        this.originalPrice = originalPrice
        this.price = price
    }

    constructor(
        id: String,
        name: String,
        image: String?,
        description: String,
        type: String,
        entryDate: Date,
        expiredDate: Date,
        quantity: Int,
        originalPrice: Float,
        price: Float
    ) {
        this.id = id
        this.name = name
        this.image = image
        this.type = type
        this.description = description
        this.entryDate = entryDate
        this.expiredDate = expiredDate
        this.quantity = quantity
        this.originalPrice = originalPrice
        this.price = price
    }
}