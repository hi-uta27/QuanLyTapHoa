package com.tavanhieu.quanlytaphoa.activities.home.domain.infra

import com.tavanhieu.quanlytaphoa.activities.home.domain.use_cases.HomeUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class HomeUseCaseImpl: HomeUseCase {
    override fun requestProductsBestSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapshot ->
            val entities = ArrayList<Product>()
            dataSnapshot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.isBestSales()) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, failure)
    }

    override fun requestProductsLeastSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", { dataSnapshot ->
            val entities = ArrayList<Product>()
            dataSnapshot.children.forEach {
                val entity = it.getValue(Product::class.java)
                if (entity != null && entity.isLeastSales()) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, failure)
    }
}