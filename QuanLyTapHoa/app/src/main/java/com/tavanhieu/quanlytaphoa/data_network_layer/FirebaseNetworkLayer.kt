package com.tavanhieu.quanlytaphoa.data_network_layer

import com.google.firebase.auth.FirebaseAuth

open class FirebaseNetworkLayer {
    companion object {
        val instance = FirebaseNetworkLayer()
    }

    private var firebaseAuth = FirebaseAuth.getInstance()

    fun authenticationWith(email: String, password: String, complete: () -> Unit, failure: () -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                complete()
            }
        }.addOnFailureListener {
            failure()
        }
    }
}