package com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case

import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Order

interface CartUseCase {
    fun refreshCart(complete: (ArrayList<Cart>) -> Unit, failure: () -> Unit)
    fun deleteCartWith(idProduct: String, complete: () -> Unit, failure: () -> Unit)
    fun createOrderWith(order: Order, carts: ArrayList<Cart>, complete: () -> Unit, failure: () -> Unit)
    fun updateCartQuantity(quantity: Int, idProduct: String, complete: () -> Unit, failure: () -> Unit)
}