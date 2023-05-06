package com.matemart.api

import com.matemart.model.login.LoginResponse
import com.matemart.model.login.UserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    // ---------Auth APIs---------------------------------------------
//    @GET("mypage/appinfo/getAppInfo")
//    suspend fun getPageData(
//        @Query("title") title: String, @Query("language") language: String
//    ): Response<ApInfoResponse>
//
//    @GET("mypage/appinfo/getAppInfo")
//    suspend fun getPageData(
//        @Query("title") title: String,
//        @Query("memberId") memberId: String,
//        @Query("language") language: String
//    ): Response<ApInfoResponse>
//
//    @POST("member/register")
//    suspend fun register(@Body body: RequestBody): Response<RegisterResponse>
//
//    @GET("member/emailVerification")
//    suspend fun verifyEmail(@Query("email") email: String): Response<EmailVerificationResponse>
//
//    @POST("member/createMemberPassword")
//    suspend fun createMemberPassword(@Body body: RequestBody): Response<RegisterResponse>
//
    @POST("send-otp")
    suspend fun login(@Body body: RequestBody): Response<LoginResponse>

    @POST("verify-otp")
    suspend fun verify_otp(@Body body: RequestBody): Response<UserResponse>
//
//    @POST("member/guestLogin")
//    suspend fun guestLogin(@Body body: RequestBody): Response<RegisterResponse>
//
//    @POST("member/socialLogin")
//    suspend fun socialLogin(@Body body: RequestBody): Response<RegisterResponse>
//
//    @POST("member/forgotPassword")
//    suspend fun forgotPassword(@Body body: RequestBody): Response<RegisterResponse>
//
//    @POST("member/changePassword")
//    suspend fun changePassword(@Body body: RequestBody): Response<RegisterResponse>
//
//    @GET("member/tokenData")
//    suspend fun getMemberTokenInfo(@Query("token") token: String): Response<MemberInfoResponse>
//
//// ---------device APIs---------------------------------------------
//
//    @GET("device/list")
//    suspend fun fetchDeviceList(@Query("language") language: String): Response<DeviceListResponse>
//
//    @POST("device/registration")
//    suspend fun deviceRegistration(@Body body: RequestBody): Response<SimpleResponse>
//
//    @GET("device/validate")
//    suspend fun validateDevice(@Query("deviceSerialNumber") deviceSerialNumber: String): Response<ValidateDeviceResponse>
//
//    @GET("device/getSingleDeviceInfo/{id}")
//    suspend fun getSingleDeviceInfo(
//        @Path("id") id: String,
//        @Query("language") language: String
//    ): Response<DeviceDetailResponse>
//
//    @POST("device/removeSerialNumber")
//    suspend fun removeSerialNumber(@Body body: RequestBody): Response<SimpleResponse>
//
//
//// --------Diving Experience- APIs---------------------------------------------
//
//    @GET("mypage/myinfo/listOrganization")
//    suspend fun listOrganization(
//        @Query("divingMode") divingMode: String,
//        @Query("language") language: String
//    ): Response<OrganizationResponse>
//
//    @GET("mypage/myinfo/listDivingLevel")
//    suspend fun listDivingLevel(@Query("organizationId") organizationId: String): Response<DivingLevelsResponse>
//
//    @POST("member/saveProfileName")
//    suspend fun saveProfileName(@Body body: RequestBody): Response<SimpleResponse>
//
//    @POST("member/saveDivingExperience")
//    suspend fun saveDivingExperience(@Body body: RequestBody): Response<SimpleResponse>
//
//    @POST("member/logout")
//    suspend fun logout(): Response<SimpleResponse>
//
//
//// ---------My Page APIs---------------------------------------------
//
//    @GET("member/getMemberInfo")
//    suspend fun getMemberInfo(@Query("language") language: String): Response<MemberInfoResponse>
//
//    @GET("device/getMyDevices")
//    suspend fun getMyDevices(): Response<MyDevicesResponse>
//
//    @GET("mypage/myinfo/listAvatars")
//    suspend fun listAvatars(): Response<AvatarListResponse>
//
//    @GET("mypage/unit/list")
//    suspend fun listUnits(): Response<UnitListResponse>
//
//    @POST("mypage/unit/setUnit")
//    suspend fun setUnit(@Body body: RequestBody): Response<SimpleResponse>
//
//    @GET("mypage/language/list")
//    suspend fun listLanguages(): Response<LanguageListResponse>
//
//    @POST("mypage/language/setLanguage")
//    suspend fun setLanguage(@Body body: RequestBody): Response<SimpleResponse>
//
//    @POST("mypage/myinfo/updateMemberInfo")
//    suspend fun updateMemberInfo(@Body body: RequestBody): Response<MemberInfoResponse>
//
//    @Multipart
//    @POST("mypage/myinfo/updateProfilePicture")
//    suspend fun updateProfilePicture(@Part part: MultipartBody.Part): Response<ProfilePictureResponse>
//
//    @FormUrlEncoded
//    @POST("mypage/myinfo/updateProfilePicture")
//    suspend fun updateProfilePicture(@Field("profilePictureUrl") profilePictureUrl: String): Response<ProfilePictureResponse>
//
//    @GET("mypage/myinfo/listActivityMedals")
//    suspend fun listActivityMedals(@Query("language") language: String): Response<ActivityMedalResponse>
//
//    @GET("mypage/deleteaccount/reasons")
//    suspend fun listDeleteReasons(): Response<ReasonsResponse>
//
//    @POST("mypage/deleteaccount/addReason")
//    suspend fun addReasonOther(@Body body: RequestBody): Response<SimpleResponse>
//
//    @DELETE("member/deleteMember")
//    suspend fun deleteAccount(@Body body: RequestBody): Response<DeleteAccountResponse>
//
//// ---------Review APIs---------------------------------------------
//
//    @GET("reviews/divesite-reviews-divesitedetail/{divesiteId}")
//    suspend fun diveSiteDetail(@Path("divesiteId") divesiteId: String): Response<DiveSiteDetailResponse>
//
//    @POST("reviews/divesite-list")
//    suspend fun diveSiteList(@Body body: RequestBody): Response<DiveSiteReviewsResponse>
//
//    @GET("reviews/divesite-review-detail/{id}")
//    suspend fun diveSiteReviewDetail(@Path("id") id: String): Response<ReviewDetailResponse>
//
//    @GET("reviews/divesite-reviews/{divesiteId}")
//    suspend fun diveSiteReviews(
//        @Path("divesiteId") id: String, @Query("page") page: Int, @Query("perPage") perPage: Int
//    ): Response<ReviewListResponse>
//
//    @GET("reviews/my-reviews")
//    suspend fun myReviews(): Response<MyReviewsResponse>
//
//    @GET("reviews/user-reviews/{id}")
//    suspend fun userReviews(@Path("id") id: String): Response<MyReviewsResponse>
//
//    @Multipart
//    @JvmSuppressWildcards
//    @POST("reviews/create")
//    suspend fun createReview(
//        @Part("divesiteId") divesiteId: RequestBody,
//        @Part("logbookId") logbookId: RequestBody,
//        @Part("dateOfVisit") dateOfVisit: RequestBody,
//        @Part("review") review: RequestBody,
//        @Part("starRating") starRating: RequestBody,
////        @Part("waterType") waterType: RequestBody,
////        @Part("waterEntry") waterEntry: RequestBody,
////        @Part("diveType") diveType: RequestBody,
////        @Part("visibility") visibility: RequestBody,
//        @Part part: List<MultipartBody.Part>
//    ): Response<SimpleResponse>
//
//    @Multipart
//    @JvmSuppressWildcards
//    @PUT("reviews/update/{id}")
//    suspend fun updateReview(
//        @Path("id") id: String,
//        @Part("divesiteId") divesiteId: RequestBody,
//        @Part("logbookId") logbookId: RequestBody,
//        @Part("dateOfVisit") dateOfVisit: RequestBody,
//        @Part("review") review: RequestBody,
//        @Part("starRating") starRating: RequestBody,
//        @Part("deleteId") deleteId: RequestBody,
//        @Part part: List<MultipartBody.Part>,
//    ): Response<SimpleResponse>
//
//    @PUT("reviews/update")
//    suspend fun updateReview(
//        @Query("id") id: String, @Body body: RequestBody
//    ): Response<SimpleResponse>
//
//    @DELETE("reviews/delete/{id}")
//    suspend fun deleteReview(@Path("id") id: String): Response<SimpleResponse>
//
//    @GET("reviews/review-comments/{reviewId}")
//    suspend fun reviewAllComments(
//        @Path("reviewId") id: String, @Query("page") page: Int, @Query("perPage") perPage: Int
//    ): Response<CommentListResponse>
//
//    @GET("reviews/child-comments/{commentParentId}")
//    suspend fun reviewAllChildComments(
//        @Path("commentParentId") id: String
//    ): Response<ChildCommentResponse>
//
//
//    @POST("reviews/comment/create")
//    suspend fun createComment(
//        @Body body: RequestBody
//    ): Response<SimpleResponse>
//
//    @PUT("reviews/comment/update/{id}")
//    suspend fun updateComment(
//        @Path("id") id: String, @Body body: RequestBody
//    ): Response<SimpleResponse>
//
//    @DELETE("reviews/comment/delete/{id}")
//    suspend fun deleteComment(@Path("id") id: String): Response<SimpleResponse>
//
//    @POST("reviews/like-unlike")
//    suspend fun reviewLike(@Body body: RequestBody): Response<SimpleResponse>
//
//// ---------Home APIs---------------------------------------------
//
//    @POST("home/list")
//    suspend fun homeMainList(@Body body: RequestBody): Response<HomeResponse>
//
//    @POST("home/nearbysite")
//    suspend fun nearBySites(@Body body: RequestBody): Response<NearByResponse>
//
//    @GET("home/search/{SiteName}")
//    suspend fun searchSiteRegion(@Path("SiteName") SiteName: String): Response<SearchRegionResponse>
//
//    // ---------Dive Settings APIs---------------------------------------------
//    @GET("deviceSettings/listOxygenPercentage")
//    suspend fun listOxygenPercentage(): Response<OxygenDepthResponse>
//
//    @GET("deviceSettings/listWaterDepth")
//    suspend fun listWaterDepth(): Response<OxygenDepthResponse>
//
//    @GET("deviceSettings/listDeviceSettings")
//    suspend fun listDeviceSettings(): Response<DeviceSettingsResponse>
//
//    @GET("deviceSettings/getDivePlanner/{id}")
//    suspend fun getDivePlannerById(@Path("id") id: String): Response<DivePlannerResponse>
//
//    @PUT("deviceSettings/updateDeviceSettings")
//    suspend fun updateDeviceSettings(@Body body: RequestBody): Response<DeviceSettingsResponse>
//
//    @PUT("deviceSettings/updateDivePlanner/{id}")
//    suspend fun updateDivePlannerById(
//        @Path("id") id: String, @Body body: RequestBody
//    ): Response<DivePlannerResponse>
//
//    @POST("deviceSettings/createUnderWaterNotification")
//    suspend fun createUnderWaterNotification(@Body body: RequestBody): Response<UnderWaterNotificationResponse>
//
//    @DELETE("deviceSettings/deleteUnderWaterNotification/{id}")
//    suspend fun deleteUnderWaterNotification(@Path("id") id: String): Response<SimpleResponse>
//
//
//    // ---------Dive Site APIs---------------------------------------------
//    @Multipart
//    @POST("divesite/create")
//    suspend fun createDiveSite(
//        @Part("divingMode") divingMode: RequestBody,
//        @Part("siteLocation") siteLocation: RequestBody,
//        @Part("siteName") siteName: RequestBody,
//        @Part("siteLatitude") siteLatitude: RequestBody,
//        @Part("siteLongitude") siteLongitude: RequestBody,
//        @Part("nation") nation: RequestBody,
////        @Part("siteAddedDate") siteAddedDate: RequestBody,
//        @Part("isRecommended") isRecommended: RequestBody
//    ): Response<SimpleResponse>
//
//
//    @PUT("divesite/add-divesite-to-Logbook")
//    suspend fun linkDiveSiteWithLogbook(
//        @Query("id") id: String, @Body body: RequestBody
//    ): Response<SimpleResponse>
//
//    @GET("divesite/{id}/logbook-list")
//    suspend fun logbookListWithDiveSites(@Path("id") SiteName: String): Response<LogBookListResponse>
//
//
//    @POST("divesite/nearby-divesite")
//    suspend fun findNearByDiveSite(@Body body: RequestBody): Response<NearByDiveSiteResponseModel>
//
//
//    //---------Logbook API-------------------------------------------
//    @GET("log/gallerycollection/list")
//    suspend fun galleryCollections(): Response<GalleryCollectionResponse>
//
//    @GET("log/gallerycollection/detail/{id}")
//    suspend fun galleryCollectionDetail(@Path("id") collectionId: Int): Response<GalleryCollectionDetailResponse>
//
//    @POST("log/gallerycollection/create")
//    suspend fun createGalleryCollection(@Body body: RequestBody): Response<CreateGalleryCollectionResponse>
//
//    @PUT("log/gallerycollection/update/{id}")
//    suspend fun updateGalleryCollection(
//        @Path("id") collectionId: Int,
//        @Body body: RequestBody
//    ): Response<GalleryCollectionDetailResponse>
//
//    @DELETE("log/gallerycollection/delete/{id}")
//    suspend fun deleteGalleryCollection(@Path("id") collectionId: Int): Response<GalleryCollectionDetailResponse>
////--------Logbook API-------------------------------------------
//
//    @POST("log/list")
//    suspend fun getLogbooks(@Body body: RequestBody): Response<LogbookListResponse>
//
//
//    @GET("log/detail/{id}")
//    suspend fun getLogbookDetail(@Path("id") logbookId: Int): Response<LogbookDetailResponse>
//
//    @POST("log/create")
//    suspend fun createLogbook(@Body body: RequestBody): Response<LogbookCreateResponse>
//
//    @PUT("log/update/{id}")
//    suspend fun updateLogbook(
//        @Path("id") logbookId: Int, @Body body: RequestBody
//    ): Response<LogbookUpdateResponse>
//
//    @DELETE("log/delete/{id}")
//    suspend fun deleteLogbook(@Path("id") logbookId: Int): Response<LogbookDeleteResponse>
//
//    @PUT("log/update-bookmark/{id}")
//    suspend fun updateLogbookBookmark(
//        @Path("id") logbookId: Int, @Body body: RequestBody
//    ): Response<LogbookBookmarkResponse>
//
//    @GET("log/filters")
//    suspend fun getLogbookFilters(): Response<FiltersResponse>
//
//    @GET("log/nations")
//    suspend fun getNations(): Response<NationListResponse>
//
//    @POST("log/nation-logbooks")
//    suspend fun getNationsLogbook(@Body body: RequestBody): Response<NationLogbooks>
//
//    @GET("log/ids")
//    suspend fun getLogbookIds(): Response<LogbookIdResponse>
//
//    @GET("log/logdepthtime/list/{id}")
//    suspend fun getLogbookDepthTime(@Path("id") depthTimeId: Int): Response<SimpleResponse>
//
//    @POST("log/logdepthtime/list/create")
//    suspend fun createLogbookDepthTime(): Response<SimpleResponse>
//
//    @GET("log/media")
//    suspend fun getLogbookMedia(): Response<SimpleResponse>
//
//    @GET("log/media/logbook/{id}")
//    suspend fun getLogbookMedias(@Path("id") logbookMediaId: Int): Response<LogbookMediaResponse>
//
//    @Multipart
//    @POST("log/media/upload")
//    suspend fun uploadLogbookMedias(
//        @Part("LogbookId") LogbookId: RequestBody,
//        @Part("Depth") Depth: RequestBody,
//        @Part("DepthDate") DepthDate: RequestBody,
//        @Part part: List<MultipartBody.Part>
//    ): Response<LogbookUpdateResponse>
//
//
//    @DELETE("log/media/{id}")
//    suspend fun deleteLogbookMedia(@Path("id") logbookMediaId: Int): Response<MyDivingPhotoDeleteResponse>
//
//    @POST("log/divingtype/create")
//    suspend fun createDiveType(@Body body: RequestBody): Response<CreateFilterResponse>
//
//    @DELETE("log/divingtype/delete/{id}")
//    suspend fun deleteDiveType(@Path("id") id: String): Response<SimpleResponse>
//
//    @POST("log/underwaterlife/create")
//    suspend fun createUnderWaterLife(@Body body: RequestBody): Response<CreateUnderWaterResponse>
//
//    @GET("log/underwaterlife/list")
//    suspend fun getUnderWaterLives(): Response<UnderWaterLivesResponseModel>
//
//
//    @GET("log/divingtype/list")
//    suspend fun getDiveTypes(): Response<DiveTypesResponseModel>
//
//
//    @DELETE("log/underwaterlife/delete/{id}")
//    suspend fun deleteUnderWaterLife(@Path("id") id: String): Response<SimpleResponse>
//
//    /**
//     * This function used to get gallery and collection info with 1 media for preview purpose
//     * */
//    @GET("log/media/gallery")
//    suspend fun galleryMediaCollectionPreview(): Response<LogbookMediaCollectionResponse>
//
//    /**
//     * This function used in the collection fragment to display the collection card preview
//     * */
//    @GET("log/media/list")
//    suspend fun galleryMediaList(): Response<MyDivingPhotosListResponse>
//
//    @POST("log/logbookmediacollection/create")
//    suspend fun linkMediaCollection(@Body body: RequestBody): Response<LinkMediaCollection>
//
//    @DELETE("log/logbookmediacollection/delete/{id}")
//    suspend fun deleteMediaCollection(@Path("id") id: Int): Response<DeleteMediaCollectionResponse>
//
//
//    /**
//     * This function used in the collection detail fragment
//     * */
//    @GET("log/gallerycollection/detail/{id}")
//    suspend fun galleryMediaCollectionListAll(@Path("id") id: Int): Response<GetMediaCollectionResponse>
//
//
//    /**
//     * This function used in the photo collection fragment
//     * */
//    @PUT("log/media/like/{id}")
//    suspend fun likeMedia(
//        @Path("id") mediaId: Int,
//        @Body body: RequestBody
//    ): Response<LikeMediaResponse>
//
//    @GET("log/media/like/list")
//    suspend fun likeMediaList(): Response<LikedMediaListResponse>
//
//    @GET("log/media/info/{id}")
//    suspend fun getMediaInfo(@Path("id") mediaId: Int): Response<MediaInfoResponse>
//
//// --------Store API-------------------------------------------
//
//    @GET("store/current-package")
//    suspend fun getCurrentPackage(): Response<CurrentPackageResponse>
//
//    @GET("store/package-list")
//    suspend fun getPackageList(): Response<PackageListResponse>
//
//    @GET("store/package-feature-list")
//    suspend fun getPackageFeaturesList(): Response<PackageFeaturesListResponse>
//
//    @GET("store/individual-feature-list")
//    suspend fun getIndividualFeaturesList(): Response<IndividualFeatureListResponse>
//
//    @POST("store/subscription-purchase")
//    suspend fun saveSubscriptionPurchase(
//        @Body body: RequestBody,
//        @Query("language") language: String
//    ): Response<SimpleResponse>
//
//    @POST("store/individual-feature-purchase")
//    suspend fun saveFeaturePurchase(
//        @Body body: RequestBody,
//        @Query("language") language: String
//    ): Response<SimpleResponse>
//
//
//    @GET("store/store-payment-history")
//    suspend fun storePaymentHistory(): Response<PaymentHistoryResponse>
//
//    @POST("store/subscription-cancel")
//    suspend fun subscriptionCancel(
//        @Body body: RequestBody,
//        @Query("language") language: String
//    ): Response<SimpleResponse>
//
//    @POST("store/refund")
//    suspend fun subscriptionRefund(
//        @Body body: RequestBody,
//        @Query("language") language: String
//    ): Response<SimpleResponse>
//
//    @GET("mypage/cancelsubscription/reasons")
//    suspend fun cancelSubscriptionReasons(): Response<ReasonsResponse>
//
//    // --------Notice API-------------------------------------------
//
//    @GET("mypage/notice/listNotice")
//    suspend fun listNotice(@Query("language") language: String): Response<NoticeListResponse>
//
//    @GET("mypage/notice/singleNotice/{id}")
//    suspend fun singleNotice(@Path("id") id: String): Response<NoticeDetailResponse>
//
//    @GET("mypage/suggestion/listSuggestionType")
//    suspend fun listSuggestionType(): Response<SuggestionListResponse>
//
//
//    @Multipart
//    @POST("mypage/suggestion/createSuggestion")
//    suspend fun createSuggestion(
//        @Part("suggestionTypeId") suggestionTypeId: RequestBody,
//        @Part("title") title: RequestBody,
//        @Part("content") content: RequestBody,
//        @Part part: List<MultipartBody.Part>
//    ): Response<SimpleResponse>
//
//
//    // --------FAQ API-------------------------------------------
//
//    @GET("mypage/faq/listFAQCategories")
//    suspend fun listFAQCategories(@Query("language") language: String): Response<FaqCategoryResponse>
//
//    @GET("mypage/faq/listFAQs")
//    suspend fun listFAQs(@Query("category") category: Long): Response<FaqOneCategoryWiseResponse>
//
//    @GET("mypage/faq/singleFAQInfo/{id}")
//    suspend fun singleFAQInfo(@Path("id") id: String): Response<FaqDetailResponse>
//
//    @GET("mypage/faq/categoryWiseFAQs")
//    suspend fun categoryWiseFAQs(@Query("language") language: String): Response<FaqCategoryWiseResponse>
//
//    @POST("mypage/faq/sendFeedback")
//    suspend fun sendFeedback(@Body body: RequestBody): Response<SimpleResponse>
//
//
//// ---------Other APIs---------------------------------------------
//
//    @GET("movie/popular")
//    suspend fun getPopularMoviesList(@Query("page") page: Int): Response<MoviesListResponse>
//
//    @GET("movie/{movie_id}")
//    suspend
//
//    fun getMovieDetails(@Path("movie_id") id: Int): Response<MovieDetailsResponse>
}