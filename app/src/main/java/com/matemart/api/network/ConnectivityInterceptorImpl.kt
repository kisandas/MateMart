package com.matemart.api.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.matemart.utils.MyApplication
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject

class ConnectivityInterceptorImpl @Inject constructor() : ConnectivityInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) {
            val jsonData =
                JSONObject("{\"status\":false,\"message\":\"No Internet connection.\",\"code\":600}")
            val mediaType = "application/json; charset=utf-8".toMediaType()

            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .headers(chain.request().headers)
                .body(ResponseBody.create(mediaType, jsonData.toString()))
                .code(600)
                .message("No Internet connection")
                .build()
        }
        return try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            // This is where your UI code goes.
            val jsonData =
                JSONObject("{\"status\":false,\"message\":\"" + e.message.toString() + "\",\"code\":601}")
            val mediaType = "application/json; charset=utf-8".toMediaType()

            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .headers(chain.request().headers)
                .body(ResponseBody.create(mediaType, jsonData.toString()))
                .code(601)
                .message(e.message.toString())
                .build()
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = MyApplication.getInstance().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        var res = false
        connectivityManager.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                res = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return res
    }
}

interface ConnectivityInterceptor : Interceptor
