package com.tavanhieu.quanlytaphoa.activities.search.domain.infra

import com.tavanhieu.quanlytaphoa.activities.search.domain.use_cases.SearchUseCase
import com.tavanhieu.quanlytaphoa.commons.compareDate
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.util.*
import kotlin.collections.ArrayList

class SearchUseCaseImpl: SearchUseCase {
    override fun searchProductById(id: String, complete: (Product) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products/${id}", {
             val entity = it.getValue(Product::class.java)
            if (entity != null) {
                complete(entity)
            }
        }, failure)
    }

    override fun searchProductByName(name: String, complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", {
            val entities = ArrayList<Product>()
            it.children.forEach {  item ->
                val entity = item.getValue(Product::class.java)
                if (entity != null && entity.name.uppercase().contains(name.uppercase())) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, failure)
    }

    override fun searchOrderByDate(date: Date, complete: (ArrayList<Order>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Orders", {
            val entities = ArrayList<Order>()
            it.children.forEach {  item ->
                val entity = item.getValue(Order::class.java)
                if (entity != null && entity.date.compareDate(date)) {
                    entities.add(entity)
                }
            }
            complete(entities)
        }, failure)
    }
}