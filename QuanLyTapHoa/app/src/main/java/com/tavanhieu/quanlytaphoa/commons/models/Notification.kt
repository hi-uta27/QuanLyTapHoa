package com.tavanhieu.quanlytaphoa.commons.models

import androidx.room.TypeConverters
import java.util.Date

class Notification {
    var id: String = ""
    var title: String = ""
    var message: String = ""
    var idProduct: String = ""

    @TypeConverters(DateConverter::class)
    var date: Date = Date()
}