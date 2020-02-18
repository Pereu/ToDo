package com.example.mytodo.settings

import androidx.lifecycle.ViewModel
import com.example.mytodo.data.source.TasksRepository

class SettingsViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {
}