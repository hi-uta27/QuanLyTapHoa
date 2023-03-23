package com.tavanhieu.quanlytaphoa.commons.models

import java.util.Date

class Employee {
    var uid: String = ""
    var email: String = ""
    var name: String = ""
    var gender: String = ""
    var address: String = ""
    var phoneNumber: String = ""
    var dateOfWork: Date = Date()

    constructor()
    constructor(uid: String, email: String, name: String, gender: String, address: String, phoneNumber: String, dateOfWork: Date) {
        this.uid = uid
        this.email = email
        this.name = name
        this.gender = gender
        this.address = address
        this.phoneNumber = phoneNumber
        this.dateOfWork = dateOfWork
    }

    constructor(uid: String, email: String, name: String, dateOfWork: Date) {
        this.uid = uid
        this.email = email
        this.name = name
        this.dateOfWork = dateOfWork
    }
}