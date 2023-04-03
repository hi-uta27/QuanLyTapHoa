package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra

import android.net.Uri
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases.DetailProductUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.util.Timer
import kotlin.concurrent.schedule

class DetailProductUseCaseImpl: DetailProductUseCase {
    override fun readProductWith(id: String, complete: (Product) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products/${id}", {
            val product = it.getValue(Product::class.java)
            if (product != null) {
                complete(product)
            }
        }, failure)
    }

    override fun updateProduct(product: Product, uriImage: Uri?, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(product, "Products/${product.id}", {
                if (uriImage == null) {
                    complete()
                } else {
                    addImageProduct(product, uriImage, complete, failure)
                }
            }, failure)
    }

    private fun addImageProduct(product: Product, uriImage: Uri, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.uploadImage(uriImage,
            "Products/${product.id}",
            "Products/${product.id}/image", complete, failure)
    }

    override fun deleteProductWith(id: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.deleteRequest("Products/${id}", complete, failure)
    }

    override fun addProductToCartWith(cart: Cart, complete: (Boolean) -> Unit, failure: () -> Unit) {
        var oldCart: Cart? = null
        FirebaseNetworkLayer.instance.getRequest("Carts/${cart.product.id}", {
            oldCart = it.getValue(Cart::class.java)
        }, failure)

        Timer().schedule(1000) {
            if (oldCart == null) {
                handelAddProductToCart(cart, { complete(true) }, failure)
            } else {
                handelUpdateQuantityToCart(cart, oldCart!!, { complete(false) }, failure)
            }
        }
    }

    private fun handelAddProductToCart(cart: Cart, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(cart, "Carts/${cart.product.id}", complete, failure)
    }

    private fun handelUpdateQuantityToCart(cart: Cart, oldCart: Cart, complete: () -> Unit, failure: () -> Unit) {
        var quantity = cart.quantity + oldCart.quantity
        if (quantity > cart.product.quantity) {
            quantity = cart.product.quantity
        }
        FirebaseNetworkLayer.instance.postRequest(quantity, "Carts/${cart.product.id}/quantity", complete, failure)
    }
}