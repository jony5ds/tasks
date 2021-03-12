package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    val mTaskRepository = TaskRepository(application)

    private val mList = MutableLiveData<List<TaskModel>>()
    val tasks = mList

    fun getAllTasks() {
       mTaskRepository.getAllTasks(object : RequestListener<List<TaskModel>> {
           override fun onSuccess(model: List<TaskModel>) {
            mList.value = model
           }

           override fun onFailure(message: String) {
            mList.value = arrayListOf()
           }

       })
    }


}