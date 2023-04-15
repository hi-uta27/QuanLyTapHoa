package com.tavanhieu.quanlytaphoa.activities.add_product.domain.use_case

import android.net.Uri
import com.tavanhieu.quanlytaphoa.commons.models.Product

interface AddProductUseCase {
    fun addProduct(product: Product, complete: () -> Unit, failure: () -> Unit)
    fun addImageProduct(product: Product, uriImage: Uri, complete: () -> Unit, failure: () -> Unit)
}