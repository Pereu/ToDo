package com.example.mytodo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytodo.data.Task

/**
 * The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Task::class], version = 1, exportSchema = true)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun taskDao(): TasksDao
}