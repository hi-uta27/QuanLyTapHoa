package com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case

import com.tavanhieu.quanlytaphoa.commons.models.Cart

interface CartUseCase {
    fun refreshCart(complete: (ArrayList<Cart>) -> Unit, failure: () -> Unit)
    fun deleteCartWith(idProduct: String, complete: () -> Unit, failure: () -> Unit)
    fun deleteAllCart(complete: () -> Unit, failure: () -> Unit)
}