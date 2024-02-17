package matemart.material.Interior.api

import matemart.material.Interior.model.login.LoginResponse
import matemart.material.Interior.model.login.UserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("send-otp")
    suspend fun login(@Body body: RequestBody): Response<LoginResponse>

    @POST("verify-otp")
    suspend fun verify_otp(@Body body: RequestBody): Response<UserResponse>

}