package com.tavanhieu.quanlytaphoa.activities.add_product.domain.infra

import android.net.Uri
import com.tavanhieu.quanlytaphoa.activities.add_product.domain.use_case.AddProductUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class AddProductUseCaseImpl: AddProductUseCase {
    override fun addProduct(product: Product, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(
            product, "Products/${product.id}", complete, failure)
    }

    override fun addImageProduct(product: Product, uriImage: Uri, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.uploadImage(uriImage,
            "Products/${product.id}",
            "Products/${product.id}/image", complete, failure)
    }
}