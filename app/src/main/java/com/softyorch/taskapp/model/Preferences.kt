package com.softyorch.taskapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "preferences_tbl")
data class Preferences(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "bool_pref_one")
    var lightDarkAutomaticTheme: Boolean = false,

    @ColumnInfo(name = "bool_pref_two")
    var lightOrDarkTheme: Boolean = false,

    @ColumnInfo(name = "bool_pref_three")
    var automaticLanguage: Boolean = false,

    @ColumnInfo(name = "bool_pref_four")
    var automaticColors: Boolean = false,

    @ColumnInfo(name = "bool_pref_five")
    var preferenceBooleanFive: Boolean = false,


    )
