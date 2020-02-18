package com.example.mytodo

import android.app.Application
import com.example.mytodo.data.source.TasksRepository
import timber.log.Timber

class TodoApplication : Application(){

    val taskRepository: TasksRepository
        get() = ServiceInjector.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}