package com.tavanhieu.quanlytaphoa.activities.order.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Order

interface OrderUseCase {
    fun refresh(complete: (ArrayList<Order>) -> Unit, failure: () -> Unit)
}