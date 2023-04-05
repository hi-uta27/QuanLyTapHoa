package com.tavanhieu.quanlytaphoa.activities.detail_order.domain.infra

import com.tavanhieu.quanlytaphoa.activities.detail_order.domain.use_case.DetailOrderUseCase
import com.tavanhieu.quanlytaphoa.commons.models.*
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class DetailOrderUseCaseImpl: DetailOrderUseCase {
    override fun readDetailOrderWith(idOrder: String, complete: (Order) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Orders/${idOrder}", {
            val order = it.getValue(Order::class.java)
            order?.let { complete(order) }
        }, failure)
    }

    override fun readEmployeeWith(order: Order, complete: (Employee) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Employee", {
            val employee = it.getValue(Employee::class.java)
            if (employee != null) {
                complete(employee)
            }
        }, failure)
    }
}