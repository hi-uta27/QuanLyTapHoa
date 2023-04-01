package com.tavanhieu.quanlytaphoa.activities.cart.domain.infra

import com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case.CartUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Cart
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

    override fun deleteAllCart(complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.deleteRequest("Carts", {
            complete()
        }, {
            failure()
        })
    }
}