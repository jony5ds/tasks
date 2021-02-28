package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.RequestListener
import com.example.tasks.service.repository.AuthenticationRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.widget.ValidationResponse

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Faz login usando API
     */
    private val mAuthenticationRepository = AuthenticationRepository(application)
    private val mPriorityRepository = PriorityRepository(application)
    private val mPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationResponse>()
    val login = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    val loggedUser = mLoggedUser

    fun doLogin(email: String, password: String) {
        mAuthenticationRepository.login(email, password, object : RequestListener {
            override fun onSuccess(model: HeaderModel) {
                storeUser(model)
                mLogin.value = ValidationResponse()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationResponse(message,false)
            }
        })
    }

    private fun storeUser(model: HeaderModel) {
        mPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
        mPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
        mPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val (personKey, tokenKey) = getUserStorage()
        val logged = (personKey != "" && tokenKey != "")

        if (!logged)
            mPriorityRepository.getPriority()

        mLoggedUser.value = logged
    }

    private fun getUserStorage(): Pair<String, String> {
        val personKey = mPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val tokenKey = mPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        return Pair(personKey, tokenKey)
    }

}