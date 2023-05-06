package com.matemart.api

import okhttp3.RequestBody
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) {

//    suspend fun register(body: RequestBody) = apiServices.register(body)
//    suspend fun verifyEmail(memberUniqueId: String) = apiServices.verifyEmail(memberUniqueId)
//    suspend fun createMemberPassword(body: RequestBody) = apiServices.createMemberPassword(body)
    suspend fun login(loginBody: RequestBody) = apiServices.login(loginBody)
    suspend fun register(loginBody: RequestBody) = apiServices.login(loginBody)
    suspend fun verify_otp(loginBody: RequestBody) = apiServices.verify_otp(loginBody)
//    suspend fun guestLogin(loginBody: RequestBody) = apiServices.guestLogin(loginBody)
//
//    suspend fun socialLogin(loginBody: RequestBody) = apiServices.socialLogin(loginBody)
//
//    suspend fun forgotPassword(forgotPasswordBody: RequestBody) =
//        apiServices.forgotPassword(forgotPasswordBody)
//
//    suspend fun changePassword(forgotPasswordBody: RequestBody) =
//        apiServices.changePassword(forgotPasswordBody)
//
//    suspend fun getPageData(title: String,language:String) = apiServices.getPageData(title,language)
//    suspend fun getPageDataDelete(title: String,memberId:String,language:String) = apiServices.getPageData(title,memberId,language)


}