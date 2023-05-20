package com.matemart.interfaces

import com.example.example.*
import com.google.gson.JsonObject
import com.matemart.model.*
import com.matemart.model.login.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @GET("home-screen")
    fun getHomeDataList(): Call<ResHomeList>?

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

    @POST("cart")
    fun addIntoCart(@Body jsonObject: JsonObject): Call<AddCartResponse>?


    @POST("remove-cart")
    fun removeFromCart(@Body jsonObject: JsonObject): Call<RemoveCartResponse>?

    @GET("category")
    fun getAllCategory(): Call<ResponseCategory>?

    @POST("sub-category")
    fun getSubCategory(@Body jsonObject: JsonObject): Call<ResponseCategory>?

   @POST("view-all-products")
    fun getViewAllProducts(@Body jsonObject: JsonObject): Call<ResponseProductList>?
 @POST("subcategory-products")
    fun getProductsFromSubCategory(@Body jsonObject: JsonObject): Call<ResponseProductList>?

    @POST("search")
    fun getSearch(@Body jsonObject: JsonObject): Call<ResponseSearch>?



    @POST("recently-search")
    fun getRecentSearch(@Body jsonObject: JsonObject): Call<ResponseRecentSearch>?

}