package com.softyorch.taskapp.data.database.tasks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.softyorch.taskapp.utils.DateConverter
import com.softyorch.taskapp.utils.UUIDConverter

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class TaskDatabase: RoomDatabase() {
    abstract val taskDao: TaskDatabaseDao

    companion object {
        const val TASK_DB_NAME = "tasks_tbl"
    }
}