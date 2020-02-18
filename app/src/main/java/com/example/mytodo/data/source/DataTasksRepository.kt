package com.example.mytodo.data.source

import androidx.lifecycle.LiveData
import com.example.mytodo.data.Result
import com.example.mytodo.data.Task
import kotlinx.coroutines.*

class DataTasksRepository(
    private val tasksLocalDataSource: LocalRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksRepository {

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return tasksLocalDataSource.observeTasks()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return tasksLocalDataSource.observeTask(taskId)
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        return tasksLocalDataSource.getTask(taskId)
    }


    override suspend fun getRandomTask(): Result<Task> {
        return tasksLocalDataSource.getRandomTask()
    }

    override suspend fun refreshTasks() {
        //updateTasksFromRemoteDataSource
    }

    override suspend fun completeTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.completeTask(task) }
        }
    }

    override suspend fun activateTask(task: Task) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { tasksLocalDataSource.activateTask(task) }
        }
    }

    override suspend fun clearCompletedTasks() {
        coroutineScope {
            launch { tasksLocalDataSource.clearCompletedTasks() }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        coroutineScope {
            launch { tasksLocalDataSource.deleteTask(taskId) }
        }
    }

    override suspend fun refreshTask(id: String) {
        coroutineScope {
            launch { tasksLocalDataSource.refreshTask(id) }
        }
    }

    override suspend fun saveTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.saveTask(task) }
        }
    }
}