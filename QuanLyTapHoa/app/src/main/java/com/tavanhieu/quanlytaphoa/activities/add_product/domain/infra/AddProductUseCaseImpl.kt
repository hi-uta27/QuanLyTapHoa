package com.tavanhieu.quanlytaphoa.activities.add_product.domain.infra

import com.tavanhieu.quanlytaphoa.activities.add_product.domain.use_case.AddProductUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class AddProductUseCaseImpl: AddProductUseCase {
    override fun addProduct(product: Product, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(product, "Products/${product.id}", complete, failure)
    }
}