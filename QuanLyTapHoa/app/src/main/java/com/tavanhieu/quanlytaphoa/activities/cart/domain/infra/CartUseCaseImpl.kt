package com.tavanhieu.quanlytaphoa.activities.cart.domain.infra

import com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case.CartUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class CartUseCaseImpl: CartUseCase {
    override fun refreshCart(complete: (ArrayList<Cart>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Carts", {
            val arr = ArrayList<Cart>()
            it.children.forEach { model ->
                val cart = model.getValue(Cart::class.java)
                if (cart != null) {
                    arr.add(cart)
                }
            }
            complete(arr)
        }, failure)
    }

    override fun deleteCartWith(idProduct: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.deleteRequest("Carts/${idProduct}", complete, failure)
    }

    override fun createOrderWith(order: Order, carts: ArrayList<Cart>, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(order, "Orders/${order.id}", {
            carts.forEach {
                updateProductQuantity(it.product.quantity - it.quantity, it.product.id, {}, {})
            }
            deleteAllCart()
            complete()
        }, failure)
    }

    private fun deleteAllCart() {
        FirebaseNetworkLayer.instance.deleteRequest("Carts", {}, {})
    }

    private fun updateProductQuantity(quantity: Int, idProduct: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(quantity, "Products/${idProduct}/quantity", complete, failure)
    }

    override fun updateQuantity(quantity: Int, idProduct: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(quantity, "Carts/${idProduct}/quantity", complete, failure)
    }
}