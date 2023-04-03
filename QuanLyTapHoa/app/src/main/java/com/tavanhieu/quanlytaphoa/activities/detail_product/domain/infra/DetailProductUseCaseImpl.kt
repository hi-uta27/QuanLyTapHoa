package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.infra

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases.DetailProductUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
        // TODO: Error to update -> Use kotlin coroutine to update this code
        runBlocking {
            val oldCart: Cart? = readCartFromDatabase(cart)
            if (oldCart == null) {
                handelAddProductToCart(cart, { complete(true) }, failure)
            } else {
                handelUpdateQuantityToCart(cart, oldCart, { complete(false) }, failure)
            }
        }
    }

    private suspend fun readCartFromDatabase(cart: Cart): Cart? = withContext(Dispatchers.IO) {
        val database: DatabaseReference = Firebase.database.reference
        val data = database.child("Carts/${cart.product.id}").get().await()
        return@withContext data.getValue(Cart::class.java)
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