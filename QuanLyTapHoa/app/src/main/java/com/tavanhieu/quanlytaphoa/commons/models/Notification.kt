package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.TypeConverters
import java.util.Date

class Notification {
    var id: String = ""
    var title: String = ""
    var message: String = ""
    var idProduct: String = ""
    var isRead: Boolean = false

    @TypeConverters(DateConverter::class)
    var date: Date = Date()

    constructor()
    constructor(id: String, title: String, message: String, idProduct: String, date: Date) {
        this.id = id
        this.title = title
        this.message = message
        this.idProduct = idProduct
        this.date = date
        isRead = false
    }

    fun compare(notification: Notification): Boolean {
        return title == notification.title && message == notification.message && idProduct == notification.idProduct
    }
}