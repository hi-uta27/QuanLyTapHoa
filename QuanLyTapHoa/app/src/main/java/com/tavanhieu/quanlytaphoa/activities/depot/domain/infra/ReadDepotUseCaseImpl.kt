package com.tavanhieu.quanlytaphoa.activities.depot.domain.infra

import com.tavanhieu.quanlytaphoa.activities.depot.domain.use_cases.ReadDepotUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class ReadDepotUseCaseImpl : ReadDepotUseCase {
    override fun refresh(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest(
            "Products", {
            val arr = ArrayList<Product>()
            it.children.forEach { it1 ->
                val product = it1.getValue(Product::class.java)
                if (product != null) {
                    arr.add(product)
                }
            }
            complete(arr)
        }, {
            failure()
        })
    }
    // TODO: Paging for Firebase Realtime
}