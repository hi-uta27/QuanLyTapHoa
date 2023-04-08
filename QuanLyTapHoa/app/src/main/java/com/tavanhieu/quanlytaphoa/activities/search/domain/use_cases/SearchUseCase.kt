package com.tavanhieu.quanlytaphoa.activities.search.domain.use_cases

import java.util.*

interface SearchUseCase {
    fun searchProductById(barCode: String, complete: () -> Unit, failure: () -> Unit)
    fun searchProductByName(name: String, complete: () -> Unit, failure: () -> Unit)
    fun searchOrderByDate(date: Date, complete: () -> Unit, failure: () -> Unit)
}