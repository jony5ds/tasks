package com.example.tasks.widget

data class ValidationLoginResponse(val message: String = "", val validator: Boolean = true)
{
    /*fun isSucessful(): Boolean {
        return validator
    }

    fun getErrorMessage() : String {
        return message
    }*/
}