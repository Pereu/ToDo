package com.example.mytodo.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.mytodo.data.Result
import com.example.mytodo.data.Task
import com.example.mytodo.data.source.LocalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataRepository(
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalRepository {

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return tasksDao.observeTasks().map {
            Result.Success(it)
        }
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return tasksDao.observeTaskById(taskId).map {
            Result.Success(it)
        }
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, true)
    }

    override suspend fun activateTask(task: Task) {
        tasksDao.updateCompleted(task.id, false)
    }

    override suspend fun getRandomTask(): Result<Task> = withContext(ioDispatcher) {
        return@withContext try {
            val task = tasksDao.getRandomTask()
            if (task != null) {
                Result.Success(task)
            } else {
                Result.Error(Exception("Task not found!"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTask(taskId: String) {
        tasksDao.deleteTaskById(taskId)
    }

    override suspend fun refreshTask(taskId: String) {

    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insertTask(task)
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher) {
        return@withContext try {
            val task = tasksDao.getTaskById(taskId)
            if (task != null) {
                Result.Success(task)
            } else {
                Result.Error(Exception("Task not found!"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}