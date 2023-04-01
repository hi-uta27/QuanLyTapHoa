package com.tavanhieu.quanlytaphoa.data_network_layer

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

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
        val path = "${requestCurrentUserUID()}/$child"
        firebaseDatabase.reference.child(path).setValue(model)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    complete()
                }
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun getRequest(child: String, complete: (DataSnapshot) -> Unit, failure: () -> Unit) {
        val path = "${requestCurrentUserUID()}/$child"
        firebaseDatabase.reference.child(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                complete(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                failure()
            }
        })
    }

    fun <T> putRequest(model: T, child: String, complete: () -> Unit, failure: () -> Unit) {
        val path = "${requestCurrentUserUID()}/$child"
        firebaseDatabase.reference.child(path).setValue(model)
            .addOnCompleteListener {
                complete()
            }.addOnFailureListener {
                failure()
            }
    }

    fun deleteRequest(child: String, complete: () -> Unit, failure: () -> Unit) {
        val path = "${requestCurrentUserUID()}/$child"
        firebaseDatabase.reference.child(path).removeValue()
            .addOnCompleteListener {
                complete()
            }.addOnFailureListener {
                failure()
            }
    }

    // MARK: - Firebase Storage
    private val firebaseStorage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    fun uploadImage(uriImage: Uri, childStorage: String, childDatabase: String,  complete: () -> Unit, failure: () -> Unit) {
        val pathStorage = "${requestCurrentUserUID()}/$childStorage"
        val pathDatabase = "${requestCurrentUserUID()}/$childDatabase"
        //Cập nhật uri ảnh sp từ Gallery lên Storage
        firebaseStorage.reference.child(pathStorage).putFile(uriImage)
            .addOnSuccessListener {
                //Lấy Url của sp trên Storage
                firebaseStorage.reference.child(pathStorage).downloadUrl
                    .addOnSuccessListener { url ->
                        //Cập nhật url lên realtime db
                        FirebaseDatabase.getInstance().reference
                            .child(pathDatabase)
                            .setValue(url.toString())
                            .addOnCompleteListener { complete() }
                            .addOnFailureListener { failure() }
                    }
            }
            .addOnFailureListener { failure() }
    }
}