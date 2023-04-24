package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tavanhieu.quanlytaphoa.commons.*
import java.util.Date
import java.io.Serializable
import java.util.Calendar

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
    var soldQuantity: Int = 0
    var originalPrice: Float = 0F
    var price: Float = 0F

    constructor()
    constructor(id: String, name: String, description: String, type: String, entryDate: Date,
                expiredDate: Date, quantity: Int, originalPrice: Float, price: Float) {
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

    // Use for add product
    constructor(id: String, name: String, image: String?, description: String, type: String,
                entryDate: Date, expiredDate: Date, quantity: Int, originalPrice: Float, price: Float) {
        this.id = id
        this.name = name
        this.image = image
        this.type = type
        this.description = description
        this.entryDate = entryDate
        this.expiredDate = expiredDate
        this.quantity = quantity
        soldQuantity = 0
        this.originalPrice = originalPrice
        this.price = price
    }

    // Use for update product
    constructor(id: String, name: String, image: String?, description: String, type: String, entryDate: Date,
                expiredDate: Date, quantity: Int, soldQuantity: Int, originalPrice: Float, price: Float) {
        this.id = id
        this.name = name
        this.image = image
        this.type = type
        this.description = description
        this.entryDate = entryDate
        this.expiredDate = expiredDate
        this.quantity = quantity
        this.soldQuantity = soldQuantity
        this.originalPrice = originalPrice
        this.price = price
    }

    fun checkOutExpiredDate(): Boolean {
        val currentDate = Calendar.getInstance().time
        return expiredDate.getDayOfDate().toInt() < currentDate.getDayOfDate().toInt()
                && expiredDate.getMonthOfDate().toInt() <= currentDate.getMonthOfDate().toInt()
                && expiredDate.getYearOfDate().toInt() <= currentDate.getYearOfDate().toInt()
    }

    fun checkComingExpiredDate(): Boolean {
        val currentDate = Calendar.getInstance().time
        if (expiredDate.compareMonth(currentDate) && expiredDate.compareYear(currentDate)) {
            return expiredDate.getDayOfDate().toInt() - currentDate.getDayOfDate().toInt() in 0..10
        }
        return false
    }

    fun isBestSales(): Boolean {
        return soldQuantity / (quantity + soldQuantity).toFloat() >= 0.7
    }

    fun isLeastSales(): Boolean {
        return soldQuantity / (quantity + soldQuantity).toFloat() <= 0.2
    }
}