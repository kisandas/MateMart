package com.matemart.model

import java.util.ArrayList

class StateAndCityModel(var state: String, cityList: ArrayList<String>) {
    var cityList = ArrayList<String>()

    init {
        this.cityList = cityList
    }
}