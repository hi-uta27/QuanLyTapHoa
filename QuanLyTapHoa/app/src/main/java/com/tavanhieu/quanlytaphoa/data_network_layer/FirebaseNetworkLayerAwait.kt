package com.tavanhieu.quanlytaphoa.data_network_layer

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseNetworkLayerAwait: FirebaseNetworkLayer() {
    companion object {
        val instance = FirebaseNetworkLayerAwait()
    }

    suspend fun postRequest(path: String): DataSnapshot? = withContext(Dispatchers.IO) {
        val child = "${requestCurrentUserUID()}/$path"
        return@withContext Firebase.database.reference.child(child).get().await()
    }
}