package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

interface RequestListener<T> {
    fun onSuccess(model: T)
    fun onFailure(message: String)
}