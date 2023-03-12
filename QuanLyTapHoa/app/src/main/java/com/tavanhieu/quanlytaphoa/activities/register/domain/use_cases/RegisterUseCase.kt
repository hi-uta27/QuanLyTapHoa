package com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases

interface RegisterUseCase {
    fun registerWith(email: String, password: String, complete: () -> Unit, failure: () -> Unit)
}