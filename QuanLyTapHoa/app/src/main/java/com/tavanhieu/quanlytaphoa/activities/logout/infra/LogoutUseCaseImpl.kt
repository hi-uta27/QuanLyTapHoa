package com.tavanhieu.quanlytaphoa.activities.logout.infra

import com.tavanhieu.quanlytaphoa.activities.logout.use_case.LogoutUseCase
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class LogoutUseCaseImpl: LogoutUseCase {
    override fun logoutCurrentUser() {
        FirebaseNetworkLayer.instance.logoutCurrentUser()
    }
}