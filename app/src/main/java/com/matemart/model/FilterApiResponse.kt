package com.matemart.model

import com.google.gson.JsonObject
import java.io.Serializable

data class FilterModelClass(
    val statusCode: Int?,
    val message: String?,
    val data: LinkedHashMap<String, LinkedHashMap<String, List<FilterBody>>>?
) : Serializable

data class FilterBody(
    val name: Any? ="",
    var isDisabled: Boolean = false
) : Serializable