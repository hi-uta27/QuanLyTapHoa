package com.tavanhieu.quanlytaphoa.activities.detail_order.domain.use_case

import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Employee
import com.tavanhieu.quanlytaphoa.commons.models.Order

interface DetailOrderUseCase {
    fun readDetailOrderWith(idOrder: String, complete: (Order) -> Unit, failure: () -> Unit)
    fun readEmployeeWith(order: Order, complete: (Employee) -> Unit, failure: () -> Unit)
}