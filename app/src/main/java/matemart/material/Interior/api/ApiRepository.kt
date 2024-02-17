package matemart.material.Interior.api

import okhttp3.RequestBody
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) {

    suspend fun login(loginBody: RequestBody) = apiServices.login(loginBody)
    suspend fun register(loginBody: RequestBody) = apiServices.login(loginBody)
    suspend fun verify_otp(loginBody: RequestBody) = apiServices.verify_otp(loginBody)

}