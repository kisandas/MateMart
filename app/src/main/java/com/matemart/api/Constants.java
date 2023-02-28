package com.matemart.api;

import com.matemart.model.StateAndCityModel;

import java.util.ArrayList;

public class Constants {

    public static String BASE_URL ="https://www.matemart.org/api";









    public static String GET_STATE ="/get-states";
    public static String SEND_OTP ="/send-otp";
    public static String VERIFY_OTP ="/verify-otp";
    public static String CHECK_PINCODE ="/check-pincode";

   public static ArrayList<StateAndCityModel>  statList  = new ArrayList<>();
}
