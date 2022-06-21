package com.pawat.crypto.data.remote

import com.pawat.crypto.common.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    var endpoints: ApiEndpoints

    companion object {

        private var INSTANCE: ApiManager? = null

        fun getInstance() : ApiManager = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ApiManager()
        }

        private const val X_ACCESS_TOKEN = "x-access-token"
    }

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(X_ACCESS_TOKEN, Constants.CLIENT_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.HOST_NAME)
            .client(client)
            .build()

        endpoints = retrofit.create(ApiEndpoints::class.java)
    }
}