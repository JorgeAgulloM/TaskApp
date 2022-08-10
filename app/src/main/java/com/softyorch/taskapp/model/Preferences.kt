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
    var preferenceBooleanOne: Boolean = false,

    @ColumnInfo(name = "bool_pref_two")
    var preferenceBooleanTwo: Boolean = false,

    @ColumnInfo(name = "bool_pref_three")
    var preferenceBooleanThree: Boolean = false,

    @ColumnInfo(name = "bool_pref_four")
    var preferenceBooleanFour: Boolean = false,

    @ColumnInfo(name = "bool_pref_five")
    var preferenceBooleanFive: Boolean = false,


)
