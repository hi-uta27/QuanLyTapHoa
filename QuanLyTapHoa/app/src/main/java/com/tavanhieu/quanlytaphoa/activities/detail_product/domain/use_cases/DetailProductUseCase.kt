package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Product

interface DetailProductUseCase {
    fun readProductWith(id: String, complete: (Product) -> Unit, failure: () -> Unit)
    fun updateProduct(product: Product, complete: () -> Unit, failure: () -> Unit)
    fun deleteProductWith(id: String, complete: () -> Unit, failure: () -> Unit)
    fun addProductToOrderWith(id: String, complete: () -> Unit, failure: () -> Unit)
}