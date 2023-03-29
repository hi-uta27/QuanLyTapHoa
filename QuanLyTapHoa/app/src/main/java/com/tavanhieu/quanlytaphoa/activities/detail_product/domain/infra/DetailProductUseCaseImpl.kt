package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra

import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases.DetailProductUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class DetailProductUseCaseImpl: DetailProductUseCase {
    override fun readProductWith(id: String, complete: (Product) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products/${id}", {
            val product = it.getValue(Product::class.java)
            if (product != null) {
                complete(product)
            }
        }, {
            failure()
        })
    }

    override fun updateProduct(product: Product, complete: () -> Unit, failure: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(id: String, complete: () -> Unit, failure: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addProductToOrder(id: String, complete: () -> Unit, failure: () -> Unit) {
        TODO("Not yet implemented")
    }
}