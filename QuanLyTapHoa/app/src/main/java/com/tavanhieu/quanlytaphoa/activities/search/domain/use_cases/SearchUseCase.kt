package com.tavanhieu.quanlytaphoa.activities.search.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Order
import com.tavanhieu.quanlytaphoa.commons.models.Product
import java.util.*
import kotlin.collections.ArrayList

interface SearchUseCase {
    fun searchProductById(id: String, complete: (Product?) -> Unit, failure: () -> Unit)
    fun searchProductByName(name: String, complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
    fun searchOrderByDate(date: Date, complete: (ArrayList<Order>) -> Unit, failure: () -> Unit)
}