package com.softyorch.taskapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.softyorch.taskapp.utils.*
import java.util.*

@Entity(tableName = USERDATA_TBL)
data class UserData(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = USER_NAME) var username: String,
    @ColumnInfo(name = USER_EMAIL) var userEmail: String,
    @ColumnInfo(name = USER_PASS) var userPass: String,
    @ColumnInfo(name = USER_PICTURE) var userPicture: String = emptyString,
    /**
     * User Settings
     */
    @ColumnInfo(name = LAST_LOGIN) var lastLoginDate: Date? = null,
    @ColumnInfo(name = REMEMBER_ME) var rememberMe: Boolean = false,
    @ColumnInfo(name = LIGHT_DARK_AUTOMATIC) var lightDarkAutomaticTheme: Boolean = true,
    @ColumnInfo(name = LIGHT_DARK) var lightOrDarkTheme: Boolean = false,
    @ColumnInfo(name = AUTOMATIC_LANGUAGE) var automaticLanguage: Boolean = true,
    @ColumnInfo(name = AUTOMATIC_COLORS) var automaticColors: Boolean = false,
    @ColumnInfo(name = TIME_LIMIT_AUTOLOGIN) var timeLimitAutoLoading: Int = 1, //One week
    @ColumnInfo(name = TEXT_SIZE) var textSize: Int = 0
)