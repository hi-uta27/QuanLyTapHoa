package com.tavanhieu.quanlytaphoa.activities.depot.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Product

interface ReadDepotUseCase {
    fun refresh(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit)
}