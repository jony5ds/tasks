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

    fun updateTask(task: TaskModel, listener: RequestListener<Boolean>) {
        val call = mRemote.updateTask(
            id = task.id,
            dueDate = task.dueDate,
            complete = task.complete,
            description = task.description,
            priorityId = task.priorityId
        )

        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String()::class.java)
                    listener.onFailure(validation)
                }
                response.body()?.let { listener.onSuccess(it) }
            }

        })
    }

    fun getAllTasks(listener: RequestListener<List<TaskModel>>) {
        val call = mRemote.getAllTasks()
        getList(call, listener)
    }

    fun getOverdue(listener: RequestListener<List<TaskModel>>) {
        val call = mRemote.getOverdueTasks()
        getList(call, listener)
    }

    fun getNextSevenDays(listener: RequestListener<List<TaskModel>>) {
        val call = mRemote.getNextSevenDays()
        getList(call, listener)
    }

    private fun getList(call: Call<List<TaskModel>>, listener: RequestListener<List<TaskModel>>) {
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(
                        response.errorBody()!!.string(), String()::class.java
                    )
                    listener.onFailure(validation)
                } else
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
            }
        })
    }


    fun loadTask(id: Int, listener: RequestListener<TaskModel>) {
        val call = mRemote.getTask(id)
        call.enqueue(object : Callback<TaskModel> {
            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String()::class.java)
                    listener.onFailure(validation)
                }
                response.body()?.let {
                    listener.onSuccess(it)
                }
            }

        })
    }

    fun updateStatus(id: Int, complete: Boolean, listener: RequestListener<Boolean>) {
        val call = if (complete) mRemote.completeTask(id) else mRemote.undoTask(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String()::class.java)
                    listener.onFailure(validation)
                }
                response.body()?.let {
                    listener.onSuccess(it)
                }
            }

        })
    }

    fun deleteTask(id: Int, listener: RequestListener<Boolean>) {
        val call = mRemote.deleteTask(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String()::class.java)
                    listener.onFailure(validation)
                }
                response.body()?.let {
                    listener.onSuccess(it)
                }
            }

        })
    }


}