package com.tavanhieu.quanlytaphoa.activities.notifications.domain.infra

import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Notification
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class NotificationUseCaseImpl: NotificationsUseCase {
    override fun checkComingExpiredDateOfProduct(complete: (ArrayList<Product>) -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapShot ->
            val entities: ArrayList<Product> = ArrayList()
            dataSnapShot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.checkComingExpiredDate()) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, {})
    }

    override fun checkOutExpiredDateOfProduct(complete: (ArrayList<Product>) -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapShot ->
            val entities: ArrayList<Product> = ArrayList()
            dataSnapShot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.checkOutExpiredDate()) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, {})
    }

    override fun checkQuantityOfProduct(complete: (ArrayList<Product>) -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapShot ->
            val entities: ArrayList<Product> = ArrayList()
            dataSnapShot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.quantity <= 10) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, {})
    }

    override fun checkProductsBestSales(complete: (ArrayList<Product>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun checkProductsLeastSales(complete: (ArrayList<Product>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addNotification(notification: Notification) {
        FirebaseNetworkLayer.instance.getRequest("Notifications", { dataSnapShot ->
            dataSnapShot.children.forEach {
                val oldNotification = it.getValue(Notification::class.java)
                if (oldNotification == null || oldNotification.compare(notification)) {
                    return@getRequest
                }
            }
            FirebaseNetworkLayer.instance.postRequest(notification, "Notifications/${notification.id}", {}, {})
        }, {})
    }

    override fun readNotification(complete: (ArrayList<Notification>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Notifications", { dataSnapShot ->
            val entities = ArrayList<Notification>()
            dataSnapShot.children.forEach {
                val notification = it.getValue(Notification::class.java)
                if (notification != null) {
                    entities.add(notification)
                }
            }
            complete(entities)
        }, failure)
    }
}