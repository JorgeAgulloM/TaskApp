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
    @ColumnInfo(name = "user_picture") var userPicture: String? = null,
    /**
     * User Settings
     */
    @ColumnInfo(name = "last_login_date") var lastLoginDate: Date? = null,
    @ColumnInfo(name = "remember_me") var rememberMe: Boolean = false,
    @ColumnInfo(name = "light_dark_automatic_theme") var lightDarkAutomaticTheme: Boolean = true,
    @ColumnInfo(name = "light_or_dark_theme") var lightOrDarkTheme: Boolean = false,
    @ColumnInfo(name = "automatic_language") var automaticLanguage: Boolean = true,
    @ColumnInfo(name = "automatic_colors") var automaticColors: Boolean = false,
    @ColumnInfo(name = "time_limit_auto_loading") var timeLimitAutoLoading: Int = 1, //One week
    @ColumnInfo(name = "text_size") var textSize: Int = 0
)