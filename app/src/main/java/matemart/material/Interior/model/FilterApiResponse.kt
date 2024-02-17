package matemart.material.Interior.model

import java.io.Serializable

data class FilterModelClass(
    val statusCode: Int?,
    val message: String?,
    val data: LinkedHashMap<String, LinkedHashMap<String, List<FilterBody>>>?
) : Serializable

data class FilterBody(
    val name: Any? ="",
    var is_disabled: Boolean = false
) : Serializable