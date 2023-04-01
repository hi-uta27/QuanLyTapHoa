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
        }, {
            failure()
        })
    }

    override fun deleteCartWith(idProduct: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.deleteRequest("Carts/${idProduct}", {
            complete()
        }, {
            failure()
        })
    }

    override fun createOrderWith(order: Order, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(order, "Orders/${order.id}", {
            updateQuantity(order.carts, complete, failure)
        }, {
            failure()
        })
    }

    private fun updateQuantity(carts: ArrayList<Cart>, complete: () -> Unit, failure: () -> Unit) {
        carts.forEach {
            FirebaseNetworkLayer.instance.postRequest(
                (it.product.quantity - it.quantity),
                "Products/${it.product.id}/quantity", {
                deleteAllCart(complete, failure)
            }, {
                failure()
            })
        }
    }

    private fun deleteAllCart(complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.deleteRequest("Carts", {
            complete()
        }, {
            failure()
        })
    }
}