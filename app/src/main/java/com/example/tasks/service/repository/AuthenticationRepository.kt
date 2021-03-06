package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.repository.remote.AuthenticationService
import com.example.tasks.service.repository.cliente.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationRepository(val context: Context) {
    private val mRemote = RetrofitClient.createService(AuthenticationService::class.java)

    fun login(email: String, password: String, listener: RequestListener<HeaderModel>) {
        val call = mRemote.login(email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(
                        response.errorBody()!!.string(),
                        String::class.java
                    )
                    listener.onFailure(validation)
                } else
                    response.body()?.let { listener.onSuccess(it) }
            }
        })
    }

    fun createUser(
        name: String,
        email: String,
        password: String,
        listener: RequestListener<HeaderModel>
    ) {
        val call = mRemote.createUser(
            name = name,
            email = email,
            password = password,
            news = false
        )
        call.enqueue(object : Callback<HeaderModel> {
            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(
                        response.errorBody()!!.string(),
                        String::class.java
                    )
                    listener.onFailure(validation)
                } else
                    response.body()?.let { listener.onSuccess(it) }
            }
        })
    }
}