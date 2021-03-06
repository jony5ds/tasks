package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.cliente.RetrofitClient
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository(context) {
    private val mRemote = RetrofitClient.createService(PriorityService::class.java)
    private val mDataBase = TaskDatabase.getDatabase(context).priorityDAO()

    fun getPriority() {

        if (!isConnectionAvailable(context)) {
            return
        }
        val call = mRemote.getList()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) { }

            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    mDataBase.clear()
                    response.body()?.let { mDataBase.save(it) }
                }
            }
        })
    }

    fun getLocalList() = mDataBase.getList()

    fun getDescription(id: Int) = mDataBase.getDescriptionById(id)
}