package com.tavanhieu.quanlytaphoa.activities.user_info.domain.use_case

import com.tavanhieu.quanlytaphoa.commons.models.Employee

interface UserInfoUseCase {
    fun requestUseInfo(complete: (Employee?) -> Unit, failure: () -> Void)
}