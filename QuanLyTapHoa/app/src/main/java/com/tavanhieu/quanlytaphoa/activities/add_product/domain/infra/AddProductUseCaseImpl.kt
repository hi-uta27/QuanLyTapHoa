package com.tavanhieu.quanlytaphoa.activities.add_product.domain.infra

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tavanhieu.quanlytaphoa.activities.add_product.domain.use_case.AddProductUseCase
import com.tavanhieu.quanlytaphoa.commons.models.Product
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.net.URI

class AddProductUseCaseImpl: AddProductUseCase {
    override fun addProduct(product: Product, complete: () -> Unit, failure: () -> Unit) {
        FirebaseNetworkLayer.instance.postRequest(
            product,
            "Products/${product.id}",
            complete,
            failure)
    }

    override fun addImageProduct(product: Product, uriImage: Uri) {
        val firebaseStorage = FirebaseStorage.getInstance().reference
            .child("Products/${product.id}")
        //Cập nhật uri ảnh sp từ Gallery lên Storage
        firebaseStorage.putFile(uriImage)
            .addOnSuccessListener {
                //Lấy Url của sp trên Storage
                firebaseStorage.downloadUrl
                    .addOnSuccessListener { url ->
                        //Cập nhật url lên realtime db
                        FirebaseDatabase.getInstance().reference
                            .child("${FirebaseNetworkLayer.instance.requestCurrentUserUID()}/Products/${product.id}/image")
                            .setValue(url.toString())
                    }
            }
    }
}