package matemart.material.interior.model

import java.io.Serializable

class WishList : Serializable {

    var p_id: Int? = null
    var status: Int? = null
    var is_available: Int? = null
    var is_wishlist: Int? = null
    var out_of_stock_status: Int? = null
    var total_quantity: Int? = null
    var average_rating: Double? = null
    var saleprice: Double? = null
    var price: Double? = null
    var p_name: String? = null
    var sku_id: String? = null
    var image: String? = null
}