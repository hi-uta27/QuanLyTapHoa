package com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases

import com.tavanhieu.quanlytaphoa.activities.register.domain.models.Employee

interface RegisterUseCase {
    fun registerWith(email: String, password: String, complete: () -> Unit, failure: () -> Unit)
    fun addToDatabase(model: Employee, child: String, complete: () -> Unit, failure: () -> Unit)

}