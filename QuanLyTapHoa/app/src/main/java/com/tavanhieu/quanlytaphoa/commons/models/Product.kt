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
    var description: String = ""

    @TypeConverters(DateConverter::class)
    var entryDate: Date = Date()

    @TypeConverters(DateConverter::class)
    var expiredDate: Date = Date()
    var quality: Int = 0
    var entryPrice: Float = 0F
    var price: Float = 0F

    constructor()
    constructor(
        id: String,
        name: String,
        description: String,
        type: String,
        entryDate: Date,
        expiredDate: Date,
        quality: Int,
        entryPrice: Float,
        price: Float
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.type = type
        this.entryDate = entryDate
        this.expiredDate = expiredDate
        this.quality = quality
        this.entryPrice = entryPrice
        this.price = price
    }

    constructor(
        id: String,
        name: String,
        image: String?,
        type: String,
        description: String,
        entryDate: Date,
        expiredDate: Date,
        quality: Int,
        entryPrice: Float,
        price: Float
    ) {
        this.id = id
        this.name = name
        this.image = image
        this.type = type
        this.description = description
        this.entryDate = entryDate
        this.expiredDate = expiredDate
        this.quality = quality
        this.entryPrice = entryPrice
        this.price = price
    }
}