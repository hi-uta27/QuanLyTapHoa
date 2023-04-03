package com.tavanhieu.quanlytaphoa.commons.models

class Cart {
    var product: Product = Product()
    var quantity: Int = 0

    constructor()
    constructor(product: Product, quantity: Int) {
        this.product = product
        this.quantity = quantity
    }
}