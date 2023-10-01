package com.matemart.model

class ViewListModel : java.io.Serializable {

    var p_id: Int? = null
    var product_detail_id: Int? = null
    var p_name: String? = null
    var image: String? = null
    var clickId: String? = null
    var color: String? = null
    var title: String? = null
    var c_id: Int? = null
    var is_compare: Int? = null
    var saleprice: String? = null
    var price: String? = null
    var total_quantity: Int? = null
    var sku_id: String? = null
    var out_of_stock_status: String? = null
    var is_available: String? = null
    var is_wishlist: Int? = null
    var average_rating: String? = null
    var priority: String? = null
    var wishlist:List<WishListModel> = emptyList()
    var cart: CartItem?= null



}
