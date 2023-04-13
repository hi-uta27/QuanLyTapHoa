package com.tavanhieu.quanlytaphoa.activities.user_info.domain.infra

import com.tavanhieu.quanlytaphoa.activities.user_info.domain.use_case.UserInfoUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Employee
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class UserInfoUseCaseImpl : UserInfoUseCase {
    override fun requestUseInfo(complete: (Employee?) -> Unit, failure: () -> Void) {
        FirebaseNetworkLayer.instance.getRequest("Employee", {
            val employee = it.getValue(Employee::class.java)
            complete(employee)
        }, { failure() })
    }
}