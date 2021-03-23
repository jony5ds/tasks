package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository
import com.example.tasks.widget.ValidationResponse

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    val mTaskRepository = TaskRepository(application)

    private val mList = MutableLiveData<List<TaskModel>>()
    val tasks = mList

    private val mValidation = MutableLiveData<ValidationResponse>()
    val validation = mValidation

    private var mFilter = 0

    fun list(filter: Int) {
        mFilter = filter
        val listener = object : RequestListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mList.value = model
            }

            override fun onFailure(message: String) {
                mList.value = arrayListOf()
                mValidation.value = ValidationResponse(message,false)
            }

        }
        when(mFilter) {
            TaskConstants.FILTER.ALL -> mTaskRepository.getAllTasks(listener)
            TaskConstants.FILTER.NEXT -> mTaskRepository.getNextSevenDays(listener)
            TaskConstants.FILTER.EXPIRED -> mTaskRepository.getOverdue(listener)
        }
    }

    fun completeTask(id: Int) {
        updateTask(id, true)
    }

    fun undoTask(id: Int) {
        updateTask(id, false)
    }

    private fun updateTask(id: Int, complete: Boolean) {
        mTaskRepository.updateStatus(id, complete, object : RequestListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(mFilter)
            }

            override fun onFailure(message: String) {}
        })
    }

    fun deleteTask(id: Int) {
        mTaskRepository.deleteTask(id, object : RequestListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(mFilter)
                mValidation.value = ValidationResponse()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationResponse(message)
            }
        })
    }
}