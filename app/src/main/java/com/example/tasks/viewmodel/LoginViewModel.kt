package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.LoginListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.widget.ValidationLoginResponse

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Faz login usando API
     */
    private val mRepository = PersonRepository(application)
    private val mPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationLoginResponse>()
    val login = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    val loggedUser = mLoggedUser

    fun doLogin(email: String, password: String) {
        mRepository.login(email, password, object : LoginListener {
            override fun onSuccess(model: HeaderModel) {
                mPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mPreferences.store(TaskConstants.SHARED.TOKEN_KEY,model.token)
                mPreferences.store(TaskConstants.SHARED.PERSON_NAME,model.name)
                mLogin.value = ValidationLoginResponse()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationLoginResponse(message,false)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val personKey = mPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val tokenKey = mPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val logged = (personKey != "" && tokenKey != "")
        mLoggedUser.value = logged
    }

}