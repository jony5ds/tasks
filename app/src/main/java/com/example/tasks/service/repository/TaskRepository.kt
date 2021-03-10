package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.cliente.RetrofitClient
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.TasksService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) {
    private val mRemote = RetrofitClient.createService(TasksService::class.java)

    fun createTask(task: TaskModel, listener: RequestListener<Boolean>) {
        val call = mRemote.insertTask(
            priorityId = task.priorityId,
            description = task.description,
            complete = task.complete,
            dueDate = task.dueDate
        )
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(
                        response.errorBody()!!.string(),
                        String::class.java
                    )
                    listener.onFailure(validation)
                }
                response.body()?.let { listener.onSuccess(it) }
            }
        })
    }
}