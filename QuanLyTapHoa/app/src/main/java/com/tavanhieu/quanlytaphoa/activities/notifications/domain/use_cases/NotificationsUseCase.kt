package com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Product

interface NotificationsUseCase {
    fun checkExpiredDateOfProduct(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
    fun checkQuantityOfProduct(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
    fun checkProductsBestSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
    fun checkProductsLeastSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
}