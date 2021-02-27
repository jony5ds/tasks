package com.example.tasks.service.repository.cliente

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){
    companion object {
        lateinit var retrofit: Retrofit
        val URL = "http://devmasterteam.com/CursoAndroidAPI/"
        val httpClient = OkHttpClient.Builder()

        private fun getRetrofitInstance(): Retrofit {
            if(!Companion::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
            return retrofit
        }

        fun <T> createService(serviceClass: Class<T>) : T {
            return getRetrofitInstance()
                .create(serviceClass)
        }
    }
}