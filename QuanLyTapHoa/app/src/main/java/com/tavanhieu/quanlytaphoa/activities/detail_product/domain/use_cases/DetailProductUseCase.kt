package com.tavanhieu.quanlytaphoa.activities.detail_product.domain.use_cases

import android.net.Uri
import com.tavanhieu.quanlytaphoa.commons.models.Cart
import com.tavanhieu.quanlytaphoa.commons.models.Product

interface DetailProductUseCase {
    fun readProductWith(id: String, complete: (Product) -> Unit, failure: () -> Unit)
    fun updateProduct(product: Product, uriImage: Uri?, complete: () -> Unit, failure: () -> Unit)
    fun deleteProductWith(id: String, complete: () -> Unit, failure: () -> Unit)
    fun addProductToCartWith(cart: Cart, complete: () -> Unit, failure: () -> Unit)
}