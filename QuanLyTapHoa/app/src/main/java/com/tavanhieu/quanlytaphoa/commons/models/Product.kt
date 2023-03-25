package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date
import java.io.Serializable

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}

class Product: Serializable {
    @PrimaryKey
    lateinit var id: String
    lateinit var name: String
    var image: String? = null
    lateinit var type: String
    lateinit var description: String

    @TypeConverters(DateConverter::class)
    lateinit var entryDate: Date
    var expiredDate: Int = 0
    var quality: Int = 0
    var entryPrice: Float = 0F
    var price: Float = 0F
    var buyNumber: Int = 0

    constructor()
    constructor(
        id: String,
        name: String,
        description: String,
        type: String,
        entryDate: Date,
        hanSudung: Int,
        quality: Int,
        entryPrice: Float,
        price: Float
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.type = type
        this.entryDate = entryDate
        this.expiredDate = hanSudung
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
        hanSuDung: Int,
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
        this.expiredDate = hanSuDung
        this.quality = quality
        this.entryPrice = entryPrice
        this.price = price
    }
}