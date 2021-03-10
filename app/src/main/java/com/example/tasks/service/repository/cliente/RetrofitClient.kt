package com.example.tasks.service.repository.cliente

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object {
        lateinit var retrofit: Retrofit
        val URL = "http://devmasterteam.com/CursoAndroidAPI/"
        val httpClient = OkHttpClient.Builder()
        private var mPersonKey = ""
        private var mTokenKey = ""

        private fun getRetrofitInstance(): Retrofit {
            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, mPersonKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, mTokenKey)
                        .build()
                    return chain.proceed(request)
                }

            })
            if (!Companion::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
            return retrofit
        }

        fun <T> createService(serviceClass: Class<T>): T {
            return getRetrofitInstance()
                .create(serviceClass)
        }

        fun addHeader(personKey: String, tokenKey: String) {
            mPersonKey = personKey
            mTokenKey = tokenKey
        }
    }
}