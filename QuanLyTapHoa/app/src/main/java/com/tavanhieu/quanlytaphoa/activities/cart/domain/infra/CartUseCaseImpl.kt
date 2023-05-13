package com.tavanhieu.quanlytaphoa.activities.cart.domain.infra

import com.tavanhieu.quanlytaphoa.activities.cart.domain.use_case.CartUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import kotlinx.coroutines.*

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
            runBlocking {
                val scope = CoroutineScope(Dispatchers.Default)
                // Sử dụng async để thực hiện các công việc trong carts.forEach
                val jobs = carts.map { cart ->
                    scope.async {
                        updateProductQuantity(cart.product.quantity - cart.quantity, cart.product.id)
                        updateProductSoldQuantity(cart.product.soldQuantity + cart.quantity, cart.product.id)
                    }
                }
                // Đợi tất cả các công việc trong jobs hoàn thành
                jobs.awaitAll()
                deleteAllCart()

                // Sau khi các công việc trong jobs đã hoàn thành, tiếp tục thực hiện các công việc khác
                scope.launch {
                    complete()
                }
            }
        }, failure)
    }

    private fun deleteAllCart() {
        FirebaseNetworkLayer.instance.deleteRequest("Carts", {}, {})
    }

    private fun updateProductQuantity(quantity: Int, idProduct: String) {
        FirebaseNetworkLayer.instance.postRequest(quantity, "Products/${idProduct}/quantity", {}, {})
    }

    private fun updateProductSoldQuantity(quantity: Int, idProduct: String) {
        FirebaseNetworkLayer.instance.postRequest(quantity, "Products/${idProduct}/soldQuantity", {}, {})
    }

    override fun updateCartQuantity(quantity: Int, idProduct: String, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(quantity, "Carts/${idProduct}/quantity", complete, failure)
    }
}