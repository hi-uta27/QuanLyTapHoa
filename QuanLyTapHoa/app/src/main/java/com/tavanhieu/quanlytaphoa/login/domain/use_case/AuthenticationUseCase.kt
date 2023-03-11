package com.tavanhieu.quanlytaphoa.login.domain.use_case

interface AuthenticationUseCase {
    fun loginWith(email: String, password: String, complete: () -> Unit, failure: () -> Unit)
}