package com.matemart.model

class ResGetArchitectContact : java.io.Serializable {

    var statuscode: Int? = null
    var message: String? = null
    var data: ArchitectContact? = null

}

class ArchitectContact : java.io.Serializable {

    var name: String? = null
    var mobile: String? = null

}