package com.tavanhieu.quanlytaphoa.activities.home.domain.infra

import com.tavanhieu.quanlytaphoa.activities.home.domain.use_cases.HomeUseCase
import com.tavanhieu.quanlytaphoa.commons.compareMonth
import com.tavanhieu.quanlytaphoa.commons.compareYear
import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayerAwait
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class HomeUseCaseImpl: HomeUseCase {
    override fun requestProductsBestSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Orders", { dataSnapshot ->
            // Danh sách đơn hàng trong tháng
            val orders = ArrayList<Order>()
            val currentDate = Date()
            dataSnapshot.children.forEach {
                val order = it.getValue(Order::class.java)
                if (order != null && order.date.compareMonth(currentDate) && order.date.compareYear(currentDate)) {
                    orders.add(order)
                }
            }

            // Lấy danh sách các sản phẩm được bán ra trong tháng và tính tổng số lượng bán ra cho mỗi sản phẩm
            val soldProductsInMonth = orders.flatMap { it.carts }
                .groupBy { it.product.id }
                .mapValues { it.value.sumOf { cart -> cart.quantity } }

            // Sắp xếp danh sách các sản phẩm theo số lượng bán ra giảm dần và lấy ra số lượng sản phẩm bán chạy nhất cần lấy
            val bestSellingProductsInMonth = getListProduct().filter {
                soldProductsInMonth.containsKey(it.id) && it.isBestSales()
            }
            complete(bestSellingProductsInMonth as ArrayList<Product>)
        }, failure)
    }

    override fun requestProductsLeastSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Orders", { dataSnapshot ->
            // Danh sách đơn hàng trong tháng
            val orders = ArrayList<Order>()
            val currentDate = Date()
            dataSnapshot.children.forEach {
                val order = it.getValue(Order::class.java)
                if (order != null && order.date.compareMonth(currentDate) && order.date.compareYear(currentDate)) {
                    orders.add(order)
                }
            }

            // Lấy danh sách các sản phẩm được bán ra trong tháng và tính tổng số lượng bán ra cho mỗi sản phẩm
            val soldProductsInMonth = orders.flatMap { it.carts }
                .groupBy { it.product.id }
                .mapValues { it.value.sumOf { cart -> cart.quantity } }

            // Sắp xếp danh sách các sản phẩm theo số lượng bán ra giảm dần và lấy ra số lượng sản phẩm bán chạy nhất cần lấy
            val bestSellingProductsInMonth = getListProduct().filter {
                soldProductsInMonth.containsKey(it.id) && it.isLeastSales()
            }
            complete(bestSellingProductsInMonth as ArrayList<Product>)
        }, failure)
    }

    private fun getListProduct(): ArrayList<Product> {
        val products = ArrayList<Product>()
        runBlocking {
            val productSnapshot = FirebaseNetworkLayerAwait.instance.getRequest("Products")
            productSnapshot?.children?.forEach {
                val product = it.getValue(Product::class.java)
                if (product != null) {
                    products.add(product)
                }
            }
        }
        return products
    }
}