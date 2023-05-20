package com.matemart.model

class ResHomeList : java.io.Serializable {

    var statuscode: Int? = null
    var message: String? = null
    var data: List<HomeDetailModel> = emptyList()
}