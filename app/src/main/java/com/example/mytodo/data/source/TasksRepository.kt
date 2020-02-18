package com.example.mytodo.data.source

import androidx.lifecycle.LiveData
import com.example.mytodo.data.Task
import com.example.mytodo.data.Result

interface TasksRepository {

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Result<Task>

    suspend fun refreshTasks()

    suspend fun completeTask(task: Task)

    suspend fun activateTask(task: Task)

    suspend fun clearCompletedTasks()

    suspend fun deleteTask(taskId: String)

    suspend fun refreshTask(id: String)

    suspend fun saveTask(task: Task)

    suspend fun getRandomTask(): Result<Task>


}