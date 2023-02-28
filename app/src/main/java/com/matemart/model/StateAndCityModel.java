package com.matemart.model;

import java.util.ArrayList;

public class StateAndCityModel {
    String state;
    ArrayList<String> cityList  = new ArrayList<>();

    public StateAndCityModel(String state, ArrayList<String> cityList) {
        this.state = state;
        this.cityList = cityList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<String> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<String> cityList) {
        this.cityList = cityList;
    }
}
