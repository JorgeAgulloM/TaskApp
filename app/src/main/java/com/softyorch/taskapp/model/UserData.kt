package com.softyorch.taskapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "userdata_tbl")
data class UserData(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "user_name") var username: String,
    @ColumnInfo(name = "user_email") var userEmail: String,
    @ColumnInfo(name = "user_pass") var userPass: String,
    @ColumnInfo(name = "user_picture") var userPicture: String?
)


