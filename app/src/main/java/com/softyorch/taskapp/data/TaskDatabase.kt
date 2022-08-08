package com.softyorch.taskapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softyorch.taskapp.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDatabaseDao
}