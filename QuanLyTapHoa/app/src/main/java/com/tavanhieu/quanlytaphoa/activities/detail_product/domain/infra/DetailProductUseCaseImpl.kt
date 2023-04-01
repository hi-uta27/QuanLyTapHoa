package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra

import android.net.Uri
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

    override fun updateProduct(product: Product, uriImage: Uri?, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(product, "Products/${product.id}",
            {
                if (uriImage == null) {
                    complete()
                } else {
                    addImageProduct(product, uriImage, {
                        complete()
                    }, {
                        failure()
                    })
                }
            }, failure)
    }

    private fun addImageProduct(product: Product, uriImage: Uri, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.uploadImage(uriImage,
            "Products/${product.id}",
            "Products/${product.id}/image", {
                complete()
            }, {
                failure()
            })
    }

    override fun deleteProductWith(id: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.deleteRequest("Products/${id}", complete, failure)
    }

    override fun addProductToOrderWith(id: String, complete: () -> Unit, failure: () -> Unit) {
        TODO("Not yet implemented")
    }
}