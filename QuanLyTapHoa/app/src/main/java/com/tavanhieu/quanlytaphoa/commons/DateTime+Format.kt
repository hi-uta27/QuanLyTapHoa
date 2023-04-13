package com.tavanhieu.quanlytaphoa.commons

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat")
fun Date.formatDate(): String {
    return SimpleDateFormat("dd/MM/yyyy").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.formatTime(): String {
    return SimpleDateFormat("HH: mm").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.formatDateAndTime(): String {
    return SimpleDateFormat("HH: mm a - dd EEE yyyy").format(this)
}

// ----------------------------------------------------------------

@SuppressLint("SimpleDateFormat")
fun Date.getNameOfDayInWeek(): String {
    return SimpleDateFormat("EEE").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getNameOfMonth(): String {
    return SimpleDateFormat("MMM").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getNameOfDayAndMonth(): String {
    return SimpleDateFormat("dd-MMM").format(this)
}

// ----------------------------------------------------------------

@SuppressLint("SimpleDateFormat")
fun Date.getDayOfDate(): String {
    return SimpleDateFormat("dd").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getMonthOfDate(): String {
    return SimpleDateFormat("MM").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getYearOfDate(): String {
    return SimpleDateFormat("yyyy").format(this)
}

fun Date.getDaysOfWeek(): ArrayList<Date> {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.time = this
    calendar.firstDayOfWeek = Calendar.MONDAY // Set the starting day of the week
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // Pass whatever day you want to get inplace of `MONDAY`

    val days: ArrayList<Date> = ArrayList()
    days.add(calendar.time) // first day of week
    repeat(6) {
        calendar.add(Calendar.DATE, 1) // set next day
        days.add(calendar.time)
    }
    return days
}

// ----------------------------------------------------------------

fun Date.compareDate(date: Date): Boolean {
    return this.formatDate() == date.formatDate()
}

@SuppressLint("SimpleDateFormat")
fun Date.compareMonth(date: Date): Boolean {
    return this.getMonthOfDate() == date.getMonthOfDate()
}

@SuppressLint("SimpleDateFormat")
fun Date.compareYear(date: Date): Boolean {
    return this.getYearOfDate() == date.getYearOfDate()
}