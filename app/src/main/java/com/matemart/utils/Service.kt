package com.matemart.utils

import android.content.Context
import android.util.Log
import com.matemart.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Service {

    var BASE_URL = "https://www.matemart.org/api/"

    fun getHttpClient(context: Context?): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(client)
            .addInterceptor(
                getUnAuthorised(context)
            )
    }

    fun getOnlyClient(context: Context?): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(getUnAuthorised(context))
    }

    fun getAnotherUserHttpClient(context: Context?, token: String?): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(client)
            .addInterceptor(
                getAnotherUnAuthorised(context, token)
            )
    }

    fun getUnAuthorised(context: Context?): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val token = SharedPreference(context!!).getString(SharedPreference.KEY_LOGIN_TOKEN)
            val request = original.newBuilder()
                .header("Authorization", "Bearer "+SharedPreference(context).getString(SharedPreference.KEY_LOGIN_TOKEN))
                .method(original.method(), original.body())
                .build()
            val response = chain.proceed(request)
            Log.d("MyApp", "Code : " + response.code())
            if (response.code() == 401) {
                if (context != null) {
                    SharedPreference(context).logoutProfile(context)
                }
                return@Interceptor response
            }
            response
        }
    }

    fun getAnotherUnAuthorised(context: Context?, token: String?): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer" + token!!)
                .method(original.method(), original.body())
                .build()
            val response = chain.proceed(request)
            Log.d("MyApp", "Code : " + response.code())
            if (response.code() == 401) {
                if (context != null) {
                    SharedPreference(context).logoutProfile(context)
                }
                return@Interceptor response
            }
            response
        }
    }

    var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()


    fun <S> createService(serviceClass: Class<S>?, context: Context?): S {
        if (BuildConfig.DEBUG) {
            var logging: HttpLoggingInterceptor = HttpLoggingInterceptor();
            logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        val baseUrl = BASE_URL
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                getHttpClient(context).connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build()
            )
            .build()
        return builder.create(serviceClass)
    }

    fun <S> createServiceWithOutLogging(serviceClass: Class<S>?, context: Context?): S {
        val baseUrl = BASE_URL
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                getOnlyClient(context).connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS).build()
            )
            .build()
        return builder.create(serviceClass)
    }


    fun <S> createRXService(serviceClass: Class<S>?, context: Context?): S {
        val baseUrl = BASE_URL
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                getHttpClient(context).connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build()
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return builder.create(serviceClass)
    }

    val client: HttpLoggingInterceptor
        get() {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            return logging
        }
}