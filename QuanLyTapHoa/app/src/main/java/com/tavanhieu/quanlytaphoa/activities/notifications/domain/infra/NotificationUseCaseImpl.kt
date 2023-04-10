package com.tavanhieu.quanlytaphoa.activities.notifications.domain.infra

import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class NotificationUseCaseImpl: NotificationsUseCase {
    override fun checkExpiredDateOfProduct(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapShot ->
            val entities: ArrayList<Product> = ArrayList()
            dataSnapShot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.checkExpiredDate()) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, failure)
    }

    override fun checkQuantityOfProduct(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapShot ->
            val entities: ArrayList<Product> = ArrayList()
            dataSnapShot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.quantity <= 10) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, failure)
    }

    override fun checkProductsBestSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun checkProductsLeastSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        TODO("Not yet implemented")
    }
}