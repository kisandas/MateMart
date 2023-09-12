package com.matemart.api

import com.matemart.model.login.LoginResponse
import com.matemart.model.login.UserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("send-otp")
    suspend fun login(@Body body: RequestBody): Response<LoginResponse>

    @POST("verify-otp")
    suspend fun verify_otp(@Body body: RequestBody): Response<UserResponse>

}