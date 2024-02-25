package matemart.material.interior.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import matemart.material.interior.api.network.ConnectivityInterceptorImpl
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.SharedPrefHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import matemart.material.interior.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = matemart.material.interior.api.Constants.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = matemart.material.interior.api.Constants.NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(connectivityInterceptorImpl: ConnectivityInterceptorImpl) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .header(
                    "access-token",
                    SharedPrefHelper.getInstance(MyApplication.getInstance())
                        .read(SharedPrefHelper.KEY_ACCESS_TOKEN, "").toString()
                )
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }


        // check internet available or not
//        val networkInterceptor = Interceptor { chain ->
//            val isConnected: Boolean = NetworkUtil.isNetAvail(MyApplication.getInstance())
//            if (!isConnected) {
//                throw NoInternetException() // Throwing our custom exception 'InternetException'
//            }
//            val builder = chain.request().newBuilder()
//            return@Interceptor chain.proceed(builder.build())
//        }

        OkHttpClient
            .Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(connectivityInterceptorImpl) // here it is
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .header(
                    "access-token",
                    SharedPrefHelper.getInstance(MyApplication.getInstance())
                        .read(SharedPrefHelper.KEY_ACCESS_TOKEN, "").toString()
                )
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }

        // check internet available or not
//        val networkInterceptor = Interceptor { chain ->
//            val isConnected: Boolean = NetworkUtil.isNetAvail(MyApplication.getInstance())
//            if (!isConnected) {
//                throw NoInternetException() // Throwing our custom exception 'InternetException'
//            }
//            val builder = chain.request().newBuilder()
//            return@Interceptor chain.proceed(builder.build())
//        }

        OkHttpClient
            .Builder()
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(requestInterceptor)
            .addInterceptor(connectivityInterceptorImpl) // here it is
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)
}