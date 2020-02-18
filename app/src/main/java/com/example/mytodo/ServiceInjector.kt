package com.example.mytodo

import android.content.Context
import androidx.room.Room
import com.example.mytodo.data.source.DataTasksRepository
import com.example.mytodo.data.source.LocalRepository
import com.example.mytodo.data.source.TasksRepository
import com.example.mytodo.data.source.local.LocalDataRepository
import com.example.mytodo.data.source.local.ToDoDatabase

object ServiceInjector {

    private var database: ToDoDatabase? = null

    var tasksRepository: TasksRepository? = null

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository ?: tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo = DataTasksRepository(createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): LocalRepository {
        val database = database ?: createDataBase(context)
        return LocalDataRepository(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java, "Tasks.db"
        ).build()
        database = result

        return result
    }
}