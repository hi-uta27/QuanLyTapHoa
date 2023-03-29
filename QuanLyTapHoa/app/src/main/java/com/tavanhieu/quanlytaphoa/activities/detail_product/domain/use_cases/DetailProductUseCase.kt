package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Product

interface DetailProductUseCase {
    fun readProductWith(id: String, complete: (Product) -> Unit, failure: () -> Unit)
    fun updateProduct(product: Product, complete: () -> Unit, failure: () -> Unit)
    fun deleteProduct(id: String, complete: () -> Unit, failure: () -> Unit)
    fun addProductToOrder(id: String, complete: () -> Unit, failure: () -> Unit)
}