package com.tavanhieu.quanlytaphoa.activities.login.domain.infra

import com.tavanhieu.quanlytaphoa.activities.login.domain.use_case.AuthenticationUseCase
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class AuthenticationUseCaseImpl: AuthenticationUseCase {
    override fun loginWith(
        email: String,
        password: String,
        complete: () -> Unit,
        failure: () -> Unit
    ) {
        FirebaseNetworkLayer.instance.authenticationWith(email, password, complete, failure)
    }
}