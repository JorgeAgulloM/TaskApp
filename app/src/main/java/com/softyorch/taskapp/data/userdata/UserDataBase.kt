package com.softyorch.taskapp.data.userdata

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.utils.DateConverter
import com.softyorch.taskapp.utils.UUIDConverter

@Database(entities = [UserData::class], version = 4, exportSchema = false)
@TypeConverters(DateConverter::class, UUIDConverter::class)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDataDao(): UserDataBaseDao
}