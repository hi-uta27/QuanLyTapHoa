package com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Product

interface NotificationsUseCase {
    fun checkProduct(complete: (String) -> Unit, failure: () -> Unit)
    fun checkProductsBestSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
    fun checkProductsLeastSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
}