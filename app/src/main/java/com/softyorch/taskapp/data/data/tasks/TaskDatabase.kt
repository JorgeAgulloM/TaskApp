package com.softyorch.taskapp.data.data.tasks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.utils.DateConverter
import com.softyorch.taskapp.utils.UUIDConverter

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDatabaseDao
}