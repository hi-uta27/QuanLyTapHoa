package com.tavanhieu.quanlytaphoa.activities.depot.domain.infra

import com.tavanhieu.quanlytaphoa.activities.depot.domain.use_cases.ReadDepotUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class ReadDepotUseCaseImpl : ReadDepotUseCase {
    override fun refresh(complete: (ArrayList<Product>) -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.getRequest("Products", {
            complete(it)
        }, {
            failure()
        })
    }
    // TODO: Paging for Firebase Realtime
}