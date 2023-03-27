package com.tavanhieu.quanlytaphoa.activities.register.domain.infra

import com.tavanhieu.quanlytaphoa.commons.models.Employee
import com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases.RegisterUseCase
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class RegisterUseCaseImpl: RegisterUseCase {
    override fun registerWith(
        email: String,
        password: String,
        complete: () -> Unit,
        failure: () -> Unit
    ) {
        FirebaseNetworkLayer.instance.registerWith(email, password, complete, failure)
    }

    override fun addToDatabase(
        model: Employee,
        complete: () -> Unit,
        failure: () -> Unit
    ) {
        FirebaseNetworkLayer.instance.postRequest(model, "${FirebaseNetworkLayer.instance.requestCurrentUserUID()}/Employee", complete, failure)
    }
}