package com.example.tasks.service.repository.cliente

import okhttp3.OkHttpClient
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
           /* httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .build()
                    return chain.proceed(request)

                }

            })*/
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