package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.PriorityRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityList = PriorityRepository(application)
    private val mList = MutableLiveData<List<PriorityModel>>()
    val getList = mList

    fun listPriorities() {
       mList.value = mPriorityList.getLocalList()
    }



}