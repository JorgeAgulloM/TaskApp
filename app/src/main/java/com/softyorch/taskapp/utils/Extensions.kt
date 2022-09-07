package com.softyorch.taskapp.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormatted(): String {
    return this.toStringFormatDate() +
            " at " +
            this.toStringFormatHour()
}

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormatDate(): String =
    SimpleDateFormat("yyyy-MM-dd").format(this)

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormatHour(): String =
    SimpleDateFormat("HH:mm:ss").format(this)

val Context.datastore by preferencesDataStore(name = USER_DATA)