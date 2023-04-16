package com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases

import com.tavanhieu.quanlytaphoa.commons.models.Employee

interface RegisterUseCase {
    fun registerWith(email: String, password: String, complete: () -> Unit, failure: () -> Unit)
    fun addToDatabase(model: Employee, complete: () -> Unit, failure: () -> Unit)
    fun sendVerifiedEmail(complete: () -> Unit, failure: () -> Unit)
}