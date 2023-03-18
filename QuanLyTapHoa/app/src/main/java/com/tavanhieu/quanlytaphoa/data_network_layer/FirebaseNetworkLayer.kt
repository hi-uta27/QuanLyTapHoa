package com.tavanhieu.quanlytaphoa.data_network_layer

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

open class FirebaseNetworkLayer {
    companion object {
        val instance = FirebaseNetworkLayer()
    }

    // MARK: - Firebase Auth
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun authIsLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun requestCurrentUserUID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid!!
    }

    fun logoutCurrentUser() {
        firebaseAuth.signOut()
    }

    fun authenticationWith(
        email: String,
        password: String,
        complete: () -> Unit,
        failure: () -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                complete()
            }
        }.addOnFailureListener {
            failure()
        }
    }

    fun registerWith(email: String, password: String, complete: () -> Unit, failure: () -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    complete()
                }
            }
            .addOnFailureListener {
                failure()
            }
    }

    // MARK: - Firebase Database
    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    fun <T> postRequest(model: T, child: String, complete: () -> Unit, failure: () -> Unit) {
        firebaseDatabase.reference.child(child).setValue(model)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    complete()
                }
            }
            .addOnFailureListener {
                failure()
            }
    }
}