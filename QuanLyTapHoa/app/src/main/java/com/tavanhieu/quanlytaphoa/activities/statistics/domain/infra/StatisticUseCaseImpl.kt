package com.tavanhieu.quanlytaphoa.activities.statistics.domain.infra

import com.tavanhieu.quanlytaphoa.activities.statistics.domain.enum.TimeUnit
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.use_cases.StatisticUseCase
import com.tavanhieu.quanlytaphoa.commons.compareDate
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
            filterBy(entities, timeLine, timeUnit, complete, failure)
        }, failure)
    }

    private fun filterBy(
        entities: ArrayList<Order>,
        timeLine: Date,
        timeUnit: TimeUnit,
        complete: (ArrayList<Order>) -> Unit,
        failure: () -> Unit
    ) {
        var results: ArrayList<Order> = ArrayList()
        when (timeUnit) {
            TimeUnit.day -> {
                results = entities.filter { timeLine.compareDate(it.date) } as ArrayList<Order>
                complete(results)
            }
//            TimeUnit.weak -> {
//                results = entities.filter { it.date == timeLine } as ArrayList<Order>
//                complete(results)
//            }
//            TimeUnit.month -> {
//                results = entities.filter { it.date == timeLine } as ArrayList<Order>
//                complete(results)
//            }
//            TimeUnit.year -> {
//                results = entities.filter { it.date == timeLine } as ArrayList<Order>
//                complete(results)
//            }
            else -> {
            }
        }
    }
}