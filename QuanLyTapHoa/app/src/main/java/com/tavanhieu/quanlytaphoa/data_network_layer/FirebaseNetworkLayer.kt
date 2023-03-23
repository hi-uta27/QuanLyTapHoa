package com.tavanhieu.quanlytaphoa.data_network_layer

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects

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
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    complete()
                }
            }
            .addOnFailureListener {
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

    @Suppress("UNCHECKED_CAST")
    fun <T> getRequest(child: String, complete: (ArrayList<T>) -> Unit, failure: () -> Unit) {
        firebaseDatabase.reference.child(child).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val arr = ArrayList<T>()
                snapshot.children.forEach {
                    val model = it as T
                    if (model != null) {
                        arr.add(model)
                    }
                }
                complete(arr)
            }

            override fun onCancelled(error: DatabaseError) {
                failure()
            }
        })
    }
}