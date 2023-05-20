package com.matemart.model

import java.io.Serializable

class ResWishList : Serializable {

    var statuscode: Int? = null
    var message: String? = null
    var data: ArrayList<ViewListModel>? = null
}