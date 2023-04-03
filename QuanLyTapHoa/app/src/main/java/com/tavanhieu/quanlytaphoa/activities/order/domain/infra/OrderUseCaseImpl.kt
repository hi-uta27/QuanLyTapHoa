package com.tavanhieu.quanlytaphoa.activities.order.domain.infra

import com.tavanhieu.quanlytaphoa.activities.order.domain.use_cases.OrderUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class OrderUseCaseImpl: OrderUseCase {
    override fun refresh(complete: (ArrayList<Order>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Orders", {
            val arr = ArrayList<Order>()
            it.children.forEach { it1 ->
                val order = it1.getValue(Order::class.java)
                if (order != null) {
                    arr.add(order)
                }
            }
            complete(arr)
        }, failure)
    }
}