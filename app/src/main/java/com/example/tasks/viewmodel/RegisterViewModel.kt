package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.widget.ValidationResponse

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = PersonRepository(application)
    private val mPreferences = SecurityPreferences(application)

    private val mCreate = MutableLiveData<ValidationResponse>()
    val create = mCreate

    fun create(name: String, email: String, password: String) {
        mRepository.createUser(
            name = name,
            email = email,
            password = password,
            listener = object : RequestListener {
                override fun onSuccess(model: HeaderModel) {
                    mPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                    mPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                    mPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)
                    mCreate.value = ValidationResponse()
                }

                override fun onFailure(message: String) {
                    mCreate.value = ValidationResponse(message = message, validator = false)
                }
            }
        )
    }

}