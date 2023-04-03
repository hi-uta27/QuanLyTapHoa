package com.tavanhieu.quanlytaphoa.commons.models

class ProductOrder {
    var idProduct: String = ""
    var quantity: Int = 0

    constructor()
    constructor(idProduct: String, quantity: Int) {
        this.idProduct = idProduct
        this.quantity = quantity
    }
}