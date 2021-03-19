package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository
import com.example.tasks.widget.ValidationResponse

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityList = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mList = MutableLiveData<List<PriorityModel>>()
    val getList = mList

    private val mValidation = MutableLiveData<ValidationResponse>()
    val validation = mValidation

    private val mTask = MutableLiveData<TaskModel>()
    val task = mTask

    fun listPriorities() {
        mList.value = mPriorityList.getLocalList()
    }

    fun saveTask(task: TaskModel) {
        if (task.id == 0) {
            mTaskRepository.createTask(task, object : RequestListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mValidation.value = ValidationResponse()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationResponse(message = message, validator = false)
                }
            })
            return
        }

        mTaskRepository.updateTask(task, object : RequestListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mValidation.value = ValidationResponse()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationResponse(message = message,validator = false)
            }

        })
    }

    fun loadTask(id: Int) {
        mTaskRepository.loadTask(id, object : RequestListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                mTask.value = model
            }

            override fun onFailure(message: String) {

            }

        })
    }


}