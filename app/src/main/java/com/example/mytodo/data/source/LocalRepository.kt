package com.example.mytodo.data.source

import androidx.lifecycle.LiveData
import com.example.mytodo.data.Result
import com.example.mytodo.data.Task

interface LocalRepository {

    fun observeTasks(): LiveData<Result<List<Task>>>
    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun clearCompletedTasks()
    suspend fun completeTask(task: Task)
    suspend fun activateTask(task: Task)
    suspend fun getRandomTask(): Result<Task>
    suspend fun deleteTask(taskId: String)
    suspend fun refreshTask(taskId: String)
    suspend fun saveTask(task: Task)
    suspend fun getTask(taskId: String): Result<Task>
}