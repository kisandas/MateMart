package com.matemart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matemart.model.login.LoginResponse
import com.google.gson.Gson
import com.matemart.annotations.Status
import com.matemart.api.ApiRepository
import com.matemart.api.WebResponse
import com.matemart.model.login.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    val loading = MutableLiveData<Boolean>()

    //    //Api
//    val signupEmailResponse = MutableLiveData<WebResponse<RegisterResponse>>()
//    val verifyEmailResponse = MutableLiveData<WebResponse<EmailVerificationResponse>>()
//    val updatePasswordResponse = MutableLiveData<WebResponse<RegisterResponse>>()
    val loginResponse = MutableLiveData<WebResponse<LoginResponse>>()
    val userResponse = MutableLiveData<WebResponse<UserResponse>>()
//    val loginGuestResponse = MutableLiveData<WebResponse<RegisterResponse>>()
//
//    val forgotPasswordResponse = MutableLiveData<WebResponse<RegisterResponse>>()
//    val appInfoResponse = MutableLiveData<WebResponse<ApInfoResponse>>()
//
//    //register email api call
//    fun register(email: String, language: String) = viewModelScope.launch {
//        val jsonData = JSONObject()
//        try {
//            jsonData.put("email", email)
//            jsonData.put("language", language)
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val requestBody = jsonData.toString().toRequestBody(mediaType)
//
//
//        loading.postValue(true)
//        val response = repository.register(requestBody)
//        if (response.isSuccessful) {//200 OK
//            val result = response.body()
//            signupEmailResponse.postValue(
//                WebResponse(
//                    Status.SUCCESS,
//                    result,
//                    result?.message
//                )
//            )
//        } else if (response.code() == 404 || response.code() == 400) {// 404 is for not found // 400 bad request
//            val result = response.errorBody()?.string()
//            result?.let {
//                val data = Gson().fromJson(it, RegisterResponse::class.java)
//                signupEmailResponse.postValue(
//                    WebResponse(
//                        Status.FAILURE,
//                        data,
//                        data.message
//                    )
//                )
//            }
//        } else
//            signupEmailResponse.postValue(
//                WebResponse(
//                    Status.SERVER_FAILURE,
//                    null,
//                    response.message()
//                )
//            )
//
//        loading.postValue(false)
//    }
//
//    //verify email api call
//    fun verifyEmail(memberUniqueId: String) = viewModelScope.launch {
//        loading.postValue(true)
//        val response = repository.verifyEmail(memberUniqueId)
//        if (response.isSuccessful) {
//            val result = response.body()
//            verifyEmailResponse.postValue(
//                WebResponse(
//                    Status.SUCCESS,
//                    result,
//                    result?.message
//                )
//            )
//        } else if (response.code() == 404 || response.code() == 400) {
//            val result = response.errorBody()?.string()
//            result?.let {
//                val data = Gson().fromJson(it, EmailVerificationResponse::class.java)
//                verifyEmailResponse.postValue(
//                    WebResponse(
//                        Status.FAILURE,
//                        data,
//                        data.message
//                    )
//                )
//            }
//
//        } else
//            verifyEmailResponse.postValue(
//                WebResponse(
//                    Status.SERVER_FAILURE,
//                    null,
//                    response.message()
//                )
//            )
//        loading.postValue(false)
//    }
//
//    //update password api call
//    fun createMemberPassword(model: AuthLoginRequest) = viewModelScope.launch {
//        val stringRequest = Gson().toJson(model)
//        val jsonData = JSONObject(stringRequest)
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val requestBody = jsonData.toString().toRequestBody(mediaType)
//
//        loading.postValue(true)
//        val response = repository.createMemberPassword(requestBody)
//
//        if (response.isSuccessful) {
//            val result = response.body()
//            updatePasswordResponse.postValue(
//                WebResponse(
//                    Status.SUCCESS,
//                    result,
//                    result?.message
//                )
//            )
//        } else if (response.code() == 400 || response.code() == 404) {
//            val result = response.errorBody()?.string()
//            result?.let {
//                val data = Gson().fromJson(it, RegisterResponse::class.java)
//                updatePasswordResponse.postValue(
//                    WebResponse(
//                        Status.FAILURE,
//                        data,
//                        data.message
//                    )
//                )
//            }
//        } else
//            updatePasswordResponse.postValue(
//                WebResponse(
//                    Status.SERVER_FAILURE,
//                    null,
//                    response.message()
//                )
//            )
//
//        loading.postValue(false)
//    }

    //login api call
    fun login(mo_no: String) = viewModelScope.launch {

        loading.postValue(true)
        val jsonData = JSONObject()
        try {
            jsonData.put("mo_no", mo_no)
//            jsonData.put("uname", uname)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonData.toString().toRequestBody(mediaType)

        loading.postValue(true)
        val response = repository.login(requestBody)
        if (response.isSuccessful) {
            val result = response.body()
            if (response.body()?.statuscode == 11) {
                loginResponse.postValue(
                    WebResponse(
                        Status.SUCCESS,
                        result,
                        result?.message
                    )
                )
            } else {
                loginResponse.postValue(
                    WebResponse(
                        Status.FAILURE,
                        result,
                        result?.message
                    )
                )
            }

        } else if (response.code() == 404 || response.code() == 400) {
            val result = response.errorBody()?.string()
            result?.let {
                val data = Gson().fromJson(it, LoginResponse::class.java)
                loginResponse.postValue(
                    WebResponse(
                        Status.FAILURE,
                        data,
                        data.message
                    )
                )
            }
        } else
            loginResponse.postValue(
                WebResponse(
                    Status.SERVER_FAILURE,
                    null,
                    response.message()
                )
            )

        loading.postValue(false)
    }

    //Register api call
    fun register(mo_no: String, uname: String) = viewModelScope.launch {

        loading.postValue(true)
        val jsonData = JSONObject()
        try {
            jsonData.put("mo_no", mo_no)
            jsonData.put("uname", uname)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonData.toString().toRequestBody(mediaType)

        loading.postValue(true)
        val response = repository.register(requestBody)
        if (response.isSuccessful) {
            val result = response.body()
            if (response.body()?.statuscode == 11) {
                loginResponse.postValue(
                    WebResponse(
                        Status.SUCCESS,
                        result,
                        result?.message
                    )
                )
            } else {
                loginResponse.postValue(
                    WebResponse(
                        Status.FAILURE,
                        result,
                        result?.message
                    )
                )
            }
        } else if (response.code() == 404 || response.code() == 400) {
            val result = response.errorBody()?.string()
            result?.let {
                val data = Gson().fromJson(it, LoginResponse::class.java)
                loginResponse.postValue(
                    WebResponse(
                        Status.FAILURE,
                        data,
                        data.message
                    )
                )
            }
        } else
            loginResponse.postValue(
                WebResponse(
                    Status.SERVER_FAILURE,
                    null,
                    response.message()
                )
            )

        loading.postValue(false)
    }

    //    Verify OTP
    fun verify_otp(token: String, verification_code: String) = viewModelScope.launch {

        loading.postValue(true)
        val jsonData = JSONObject()
        try {
            jsonData.put("token", token)
            jsonData.put("verification_code", verification_code)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonData.toString().toRequestBody(mediaType)

        loading.postValue(true)
        val response = repository.verify_otp(requestBody)
        if (response.isSuccessful) {
            val result = response.body()
            if (response.body()?.statuscode == 11) {
                userResponse.postValue(
                    WebResponse(
                        Status.SUCCESS,
                        result,
                        result?.message
                    )
                )
            } else {
                userResponse.postValue(
                    WebResponse(
                        Status.FAILURE,
                        result,
                        result?.message
                    )
                )
            }
        } else if (response.code() == 404 || response.code() == 400) {
            val result = response.errorBody()?.string()
            result?.let {
                val data = Gson().fromJson(it, UserResponse::class.java)
                userResponse.postValue(
                    WebResponse(
                        Status.FAILURE,
                        data,
                        data.message
                    )
                )
            }
        } else
            userResponse.postValue(
                WebResponse(
                    Status.SERVER_FAILURE,
                    null,
                    response.message()
                )
            )

        loading.postValue(false)
    }

//    //login api call
//    fun guestLogin(mAuthLoginRequest: AuthLoginRequest) = viewModelScope.launch {
//        val stringRequest = Gson().toJson(mAuthLoginRequest)
//        val jsonData = JSONObject(stringRequest)
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val requestBody = jsonData.toString().toRequestBody(mediaType)
//
//        loading.postValue(true)
//        val response = repository.guestLogin(requestBody)
//        if (response.isSuccessful) {
//            val result = response.body()
//            loginGuestResponse.postValue(
//                WebResponse(
//                    Status.SUCCESS,
//                    result,
//                    result?.message
//                )
//            )
//        } else if (response.code() == 404 || response.code() == 400) {
//            val result = response.errorBody()?.string()
//            result?.let {
//                val data = Gson().fromJson(it, RegisterResponse::class.java)
//                loginGuestResponse.postValue(
//                    WebResponse(
//                        Status.FAILURE,
//                        data,
//                        data.message
//                    )
//                )
//            }
//        } else
//            loginGuestResponse.postValue(
//                WebResponse(
//                    Status.SERVER_FAILURE,
//                    null,
//                    response.message()
//                )
//            )
//
//        loading.postValue(false)
//    }
//
//
//    //social login api call
//    fun socialLogin(model: AuthLoginRequest) = viewModelScope.launch {
//        val stringRequest = Gson().toJson(model)
//        val jsonData = JSONObject(stringRequest)
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val requestBody = jsonData.toString().toRequestBody(mediaType)
//
//        loading.postValue(true)
//        val response = repository.socialLogin(requestBody)
//        if (response.isSuccessful) {
//            val result = response.body()
//            loginResponse.postValue(
//                WebResponse(
//                    Status.SUCCESS,
//                    result,
//                    result?.message
//                )
//            )
//        } else if (response.code() == 404 || response.code() == 400) {
//            val result = response.errorBody()?.string()
//            result?.let {
//                val data = Gson().fromJson(it, RegisterResponse::class.java)
//                loginResponse.postValue(
//                    WebResponse(
//                        Status.FAILURE,
//                        data,
//                        data.message
//                    )
//                )
//            }
//        } else
//            loginResponse.postValue(
//                WebResponse(
//                    Status.SERVER_FAILURE,
//                    null,
//                    response.message()
//                )
//            )
//
//        loading.postValue(false)
//    }
//
//    //forgot password api call
//    fun forgotPassword(email: String, language: String) = viewModelScope.launch {
//
//        val jsonData = JSONObject()
//        try {
//            jsonData.put("email", email)
//            jsonData.put("language", language)
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val requestBody = jsonData.toString().toRequestBody(mediaType)
//
//
//        loading.postValue(true)
//        val response = repository.forgotPassword(requestBody)
//
//        if (response.isSuccessful) {
//            val result = response.body()
//            forgotPasswordResponse.postValue(
//                WebResponse(
//                    Status.SUCCESS,
//                    result,
//                    result?.message
//                )
//            )
//        } else if (response.code() == 400 || response.code() == 404) {
//            val result = response.errorBody()?.string()
//            result?.let {
//                val data = Gson().fromJson(it, RegisterResponse::class.java)
//                forgotPasswordResponse.postValue(
//                    WebResponse(
//                        Status.FAILURE,
//                        data,
//                        data.message
//                    )
//                )
//            }
//        } else
//            forgotPasswordResponse.postValue(
//                WebResponse(
//                    Status.SERVER_FAILURE,
//                    null,
//                    response.message()
//                )
//            )
//
//        loading.postValue(false)
//    }
//
//
//    //change password api call
//    fun changePassword(email: String, password: String, newPassword: String) =
//        viewModelScope.launch {
//
//            val jsonData = JSONObject()
//            try {
//                jsonData.put("email", email)
//                jsonData.put("password", password)
//                jsonData.put("newPassword", newPassword)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//            val mediaType = "application/json; charset=utf-8".toMediaType()
//            val requestBody = jsonData.toString().toRequestBody(mediaType)
//
//
//            loading.postValue(true)
//            val response = repository.changePassword(requestBody)
//
//            if (response.isSuccessful) {
//                val result = response.body()
//                updatePasswordResponse.postValue(
//                    WebResponse(
//                        Status.SUCCESS,
//                        result,
//                        result?.message
//                    )
//                )
//            } else if (response.code() == 400 || response.code() == 404) {
//                val result = response.errorBody()?.string()
//                result?.let {
//                    val data = Gson().fromJson(it, RegisterResponse::class.java)
//                    updatePasswordResponse.postValue(
//                        WebResponse(
//                            Status.FAILURE,
//                            data,
//                            data.message
//                        )
//                    )
//                }
//            } else
//                updatePasswordResponse.postValue(
//                    WebResponse(
//                        Status.SERVER_FAILURE,
//                        null,
//                        response.message()
//                    )
//                )
//
//            loading.postValue(false)
//        }
//
//
//    //privacy_policy
//    //open_source_license
//    //delete_account
//    //terms_of_service
//    fun getPageData(title: String, language: String, memberId: String = "") =
//        viewModelScope.launch {
//            loading.postValue(true)
//            val response = if (memberId.isNotEmpty())
//                repository.getPageDataDelete(title, memberId, language)
//            else
//                repository.getPageData(title, language)
//            if (response.isSuccessful) {
//                val result = response.body()
//                appInfoResponse.postValue(
//                    WebResponse(
//                        Status.SUCCESS,
//                        result,
//                        result?.message
//                    )
//                )
//            } else if (response.code() == 404 || response.code() == 400) {
//                val result = response.errorBody()?.string()
//                result?.let {
//                    val data = Gson().fromJson(it, ApInfoResponse::class.java)
//                    appInfoResponse.postValue(
//                        WebResponse(
//                            Status.FAILURE,
//                            data,
//                            data.message
//                        )
//                    )
//                }
//
//            } else
//                appInfoResponse.postValue(
//                    WebResponse(
//                        Status.SERVER_FAILURE,
//                        null,
//                        response.message()
//                    )
//                )
//            loading.postValue(false)
//        }


}