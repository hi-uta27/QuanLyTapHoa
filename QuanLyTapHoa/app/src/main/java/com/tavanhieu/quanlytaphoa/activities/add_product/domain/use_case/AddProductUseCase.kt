package com.tavanhieu.quanlytaphoa.activities.add_product.domain.use_case

import com.tavanhieu.quanlytaphoa.commons.models.Product

interface AddProductUseCase {
    fun addProduct(product: Product, complete: () -> Unit, failure: () -> Unit)
}