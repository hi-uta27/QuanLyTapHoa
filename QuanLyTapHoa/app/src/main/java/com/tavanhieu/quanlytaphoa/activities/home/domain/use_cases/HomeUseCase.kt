package com.tavanhieu.quanlytaphoa.activities.home.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Product
import kotlin.collections.ArrayList

interface HomeUseCase {
    fun requestProductsBestSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
    fun requestProductsLeastSales(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
}