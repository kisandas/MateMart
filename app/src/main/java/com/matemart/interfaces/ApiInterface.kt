package com.matemart.interfaces

import com.google.gson.JsonObject
import com.matemart.model.*
import com.matemart.model.login.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @GET("get-wishlist")
    fun getWishList(): Call<ResWishList>?

    @GET("laboures-filter")
    fun getLabourFilter(): Call<ResGetLabourFilter>?

    @POST("laboures")
    fun getAllLabour(@Body jsonObject: JsonObject): Call<ResGetLabour>?

    @GET("architect")
    fun getArchitectList(): Call<ResGetArchitectList>?


    @POST("single-architect")
    fun getSingleArchitect(@Body jsonObject: JsonObject): Call<ResGetSingleArchitect>?

    @POST("profile-details")
    fun getUserProfile(): Call<ResGetProfileDetails>?

    @POST("update-profile")
    fun updateUserProfile(@Body jsonObject: JsonObject): Call<ResGetProfileDetails>?

    @Multipart
    @POST("profile-photo")
    fun updateProfileImage(@Part image: MultipartBody.Part): Call<ResUploadProfileImage>?

    @POST("details-architect")
    fun getArchitectDetails(@Body jsonObject: JsonObject): Call<ResGetArchitectDetails>?

    @POST("send-otp")
    fun sendOtp(@Body jsonObject: JsonObject): Call<ResSendOtp>?

    @POST("change-number-verify-otp")
    fun changeNumberVerifyOtp(@Body jsonObject: JsonObject): Call<UserResponse>?

    @POST("change-number")
    fun changeNumber(@Body jsonObject: JsonObject): Call<ResSendOtp>?

    @POST("add-remove-wishlist")
    fun updateWishList(@Body jsonObject: JsonObject): Call<JsonObject>
}