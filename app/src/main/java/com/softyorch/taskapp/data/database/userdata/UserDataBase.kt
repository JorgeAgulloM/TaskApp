package com.softyorch.taskapp.data.database.userdata

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.softyorch.taskapp.utils.DateConverter
import com.softyorch.taskapp.utils.UUIDConverter

@Database(entities = [UserDataEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDataDao(): UserDataBaseDao

    companion object {
        const val USERDATA_DB_NAME = "userdata_tbl"
    }
}