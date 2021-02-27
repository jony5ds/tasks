package com.example.tasks.service.repository.remote

import com.example.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TasksService {

    @GET("Task")
    fun getAllTasks(): Call<List<TaskModel>>

    @GET("Task/Next7Days")
    fun getNextSevenDays(): Call<List<TaskModel>>

    @GET("Task/Overdue")
    fun getOverdueTasks(): Call<List<TaskModel>>

    @GET("Task/{id}")
    fun getTask(@Path(value = "id", encoded = true) id: Int
    ): Call<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    fun insertTask(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ):Call<Boolean>

    @HTTP(method = "PUT", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun updateTask(
        @Field("id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ):Call<Boolean>


    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    @FormUrlEncoded
    fun completeTask(@Field("id") id : Int): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    @FormUrlEncoded
    fun undoTask(@Field("id") id : Int): Call<Boolean>


    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun deleteTask(@Field("id") id : Int): Call<Boolean>

}