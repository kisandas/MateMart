package com.matemart.model

class HomeDetailModel : java.io.Serializable {

    var viewId: String? = null
    var indexId: Int? = 0
    var title: String? = null
    var imageRatio: String? = null
    var image: String? = null
    var clickId: String? = null
    var viewList:List<ViewListModel> = emptyList()
    var viewAll:ViewAllModel? = null

}
