package com.matemart.model

class ResGetLabourFilter : java.io.Serializable {
    var statuscode: Int? = null
    var message: String? = null
    var data: List<LabourFilter> = emptyList()
}