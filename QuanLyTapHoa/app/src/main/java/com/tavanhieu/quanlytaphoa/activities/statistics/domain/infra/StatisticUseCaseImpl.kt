package com.tavanhieu.quanlytaphoa.activities.statistics.domain.infra

import com.tavanhieu.quanlytaphoa.activities.statistics.domain.enum.TimeUnit
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.use_cases.StatisticUseCase
import com.tavanhieu.quanlytaphoa.commons.*
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.util.*
import kotlin.collections.ArrayList

class StatisticUseCaseImpl: StatisticUseCase {
    override fun filterOrderListBy(
        timeLine: Date,
        timeUnit: TimeUnit,
        complete: (ArrayList<Order>) -> Unit,
        failure: () -> Unit
    ) {
        FirebaseNetworkLayer.instance.getRequest("Orders", {
            val entities = ArrayList<Order>()
            it.children.forEach { snapshot ->
                val order = snapshot.getValue(Order::class.java)
                if (order != null) {
                    entities.add(order)
                }
            }
            filter(entities, timeLine, timeUnit, complete, failure)
        }, failure)
    }

    private fun filter(
        entities: ArrayList<Order>,
        timeLine: Date,
        timeUnit: TimeUnit,
        complete: (ArrayList<Order>) -> Unit,
        failure: () -> Unit
    ) {
        // day -> order in day
        // weak -> all order in day
        // month -> all order in day
        // year -> all order in month
        when (timeUnit) {
            TimeUnit.day -> filterByDay(entities, timeLine, complete)
            TimeUnit.weak -> filterByWeak(entities, timeLine, complete)
            TimeUnit.month -> filterByMonth(entities, timeLine, complete)
//            TimeUnit.year -> filterByYear(entities, timeLine, complete)
            else -> failure()
        }
    }

    private fun filterByDay(entities: ArrayList<Order>,
                            timeLine: Date,
                            complete: (ArrayList<Order>) -> Unit) {
        val results = entities.filter { timeLine.compareDate(it.date) } as ArrayList<Order>
        complete(results)
    }

    private fun filterByWeak(entities: ArrayList<Order>,
                             timeLine: Date,
                             complete: (ArrayList<Order>) -> Unit) {
        // filter by weak: Mon - Sun
        // get list order in a day -> totalPrice all order in day
        val results: ArrayList<Order> = ArrayList()
        val daysOfWeek = timeLine.getDaysOfWeek()
        daysOfWeek.forEach {
                val carts = getCartInDay(entities, it.getDayOfDate().toInt())
                results.add(Order("", "", carts, it))
        }

        complete(results)
    }

    private fun filterByMonth(entities: ArrayList<Order>,
                              timeLine: Date,
                              complete: (ArrayList<Order>) -> Unit) {
        // filter by month: 1 - 31
        // get list order in a day -> totalPrice all order in day

        val entitiesInMonth = entities.filter { it.date.compareMonth(timeLine) && it.date.compareYear(timeLine) } as ArrayList<Order>
        val results: ArrayList<Order> = ArrayList()

        (1..31).forEach { day ->
            val carts = getCartInDay(entitiesInMonth, day)

            val calendar = Calendar.getInstance()
            calendar.set(
                timeLine.getYearOfDate().toInt(),
                timeLine.getMonthOfDate().toInt() - 1,
                day
            )
            results.add(Order("", "", carts, calendar.time))
        }

        complete(results)
    }

    private fun getCartInDay(entitiesInMonth: ArrayList<Order>, day: Int): ArrayList<Cart> {
        val entitiesInDay = entitiesInMonth.filter { it.date.getDayOfDate().toInt() == day }
        val carts = ArrayList<Cart>()
        entitiesInDay.forEach {
            carts.addAll(it.carts)
        }
        return carts
    }

    // TODO: I'll update get year after
//    private fun filterByYear(entities: ArrayList<Order>,
//                             timeLine: Date,
//                             complete: (ArrayList<Order>) -> Unit) {
//        // filter all year:
//        // get array(Order) in a year from database
//        // get total price in a year
//
//        val entitiesInYear = entities.filter { it.date.compareYear(timeLine) } as ArrayList<Order>
//
//        val results: ArrayList<Order> = ArrayList()
//
//        (1..12).forEach { month ->
//            val entitiesInMonth = entitiesInYear.filter { it.date.getMonthOfDate() == month.toString() }
//            val carts = ArrayList<Cart>()
//            entitiesInMonth.forEach {
//                carts.addAll(it.carts)
//            }
//
//            val calendar = Calendar.getInstance()
//            calendar.set(
//                timeLine.getYearOfDate().toInt(),
//                month-1,
//                timeLine.getDayOfDate().toInt() - 1
//            )
//            results.add(Order("", "", carts, calendar.time))
//        }
//
//        complete(results)
//    }
}