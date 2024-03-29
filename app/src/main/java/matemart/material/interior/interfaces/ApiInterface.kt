package matemart.material.interior.interfaces

import com.example.example.*
import com.google.gson.JsonObject
import matemart.material.interior.model.*
import matemart.material.interior.model.login.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @GET("home-screen")
    fun getHomeDataList(): Call<ResHomeList>?

    @GET("get-wishlist")
    fun getWishList(): Call<ResWishList>?

    @GET("laboures-filter")
    fun getLabourFilter(): Call<ResGetLabourFilter>?

    @GET("guest-user")
    fun getGuestUser(): Call<GuestUserModel>?


    @POST("laboures")
    fun getAllLabour(@Body jsonObject: JsonObject): Call<ResGetLabour>?

    @GET("architect")
    fun getArchitectList(): Call<ResGetArchitectList>?


    @POST("single-architect")
    fun getSingleArchitect(@Body jsonObject: JsonObject): Call<ResGetSingleArchitect>?

    @POST("refresh-token")
    fun refreshTokenForPush(@Body jsonObject: JsonObject): Call<CommonResponse>?

    @GET("profile-details")
    fun getUserProfile(): Call<ResGetProfileDetails>?

    @POST("update-profile")
    fun updateUserProfile(@Body body: RequestBody): Call<ResGetProfileDetails>?

    @Multipart
    @POST("profile-photo")
    fun updateProfileImage(@Part image: MultipartBody.Part): Call<ResUploadProfileImage>?

    @POST("details-architect")
    fun getArchitectDetails(@Body jsonObject: JsonObject): Call<ResGetArchitectContact>?

    @POST("single-laboures")
    fun getLabourDetails(@Body jsonObject: JsonObject): Call<ResGetArchitectContact>?


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

    @POST("product-search")
    fun getProductFromSearch(@Body jsonObject: JsonObject): Call<ResponseProductList>?


    @GET("get-cart")
    fun getCartData(): Call<CartResponseModel>?


    @GET("coupon")
    fun getCouponData(): Call<AllCouponResponse>?

    @GET("address")
    fun getAddressList(): Call<AddressListResponse>?

    @POST("address-default")
    fun markAddressDefault(@Body jsonObject: JsonObject): Call<CommonResponse>?

    @POST("address-delete")
    fun deleteAddress(@Body jsonObject: JsonObject): Call<CommonResponse>?

    @POST("address-update")
    fun updateAddress(@Body jsonObject: JsonObject): Call<CommonResponse>?

    @POST("address-store")
    fun createAddress(@Body jsonObject: JsonObject): Call<CommonResponse>?

    @GET("app-update-data")
    fun getAppUpdateData(@Header("matemart-app-platform") android: String,@Header("matemart-app-version") version: String): Call<AppDataResponse>?

    @POST("product-details")
    fun getProductDetail(@Body jsonObject: JsonObject): Call<GetProductDetailsResponse>?


   @POST("similar-products")
    fun getSimilarProduct(@Body jsonObject: JsonObject): Call<ResponseProductList>?

    @POST("compare-product")
    fun getCompareProductDetail(@Body jsonObject: JsonObject): Call<CompareProductDetailResponse>?

    @POST("compare-product-with-brand")
    fun getCompareBrand(@Body jsonObject: JsonObject): Call<CompareBrandFilter>?


    @POST("single-product-filter")
    fun getSingleProductDetail(@Body jsonObject: JsonObject): Call<GetProductDetailsResponse>?


    @POST("filter")
    fun getAllFilter(@Body jsonObject: JsonObject): Call<FilterModelClass>?

    @POST("get-filter")
    fun getSelectedAllFilter(@Body jsonObject: JsonObject): Call<FilterModelClass>?


    @POST("filter-products")
    fun getFilteredProduct(@Body jsonObject: JsonObject): Call<ResponseProductList>?


    @POST("razorpay-url")
    fun getRazorPayPaymentURL(@Body jsonObject: JsonObject): Call<RazorPayURLResponse>?

    @POST("review")
    fun getAllReview(@Body jsonObject: JsonObject): Call<ResReviewModel>?

    @POST("order-create")
    fun createOrder(@Body jsonObject: JsonObject): Call<AllOrderResponseResponse>?

    @GET("order")
    fun getAllOrderData(): Call<AllOrderResponseResponse>?

    @POST("order-cancel")
    fun cancelOrder(@Body request: JsonObject): Call<CommonResponse>?

    @POST("return-order")
    fun returnOrder(@Body request: JsonObject): Call<CommonResponse>?

    @POST("review-store")
    fun writeReview(@Body request: JsonObject): Call<CommonResponse>?

    @POST("single-order")
    fun getSingleOrderDetails(@Body request: JsonObject): Call<ResponseSingleOrderData>?


    @POST("order-tracking")
    fun getOrderTracking(@Body request: JsonObject): Call<ResponseOrderTrackingModel>?

    @POST("verify-otp-user-delete")
    fun deleteUser(@Body request: JsonObject): Call<CommonResponse>?

    @POST("user-logout")
    fun logoutUser(): Call<LogoutResponse>?

    @Multipart
    @POST("image-upload")
    fun uploadReceipt(@Part image: MultipartBody.Part): Call<ImageUploadResponseModel>?

    @GET("{ifscCode}")
    fun getIFSCDetails(@Path("ifscCode") ifscCode: String): Call<ResponseBody>

    @POST("post-requirement")
    fun postYourRequirements(
        @Part requirement_image: MultipartBody.Part,
        @Body body: JsonObject
    ): Call<CommonResponse>
}