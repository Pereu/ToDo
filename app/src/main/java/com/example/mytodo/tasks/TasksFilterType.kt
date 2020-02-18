package com.example.mytodo.tasks

enum class TasksFilterType (private val value: String) {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS("All"),

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS("Active"),

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS("Completed");



    companion object {
        fun getFilterByValue(value: String) = values().find { it.value == value } ?: ALL_TASKS
    }
}