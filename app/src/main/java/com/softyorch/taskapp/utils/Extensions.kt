package com.softyorch.taskapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormatted(date: Date): String {
    return date.toStringFormatDate(date = date) +
            " at " +
            date.toStringFormatHour(date = date)
}


@SuppressLint("SimpleDateFormat")
fun Date.toStringFormatDate(date: Date): String =
    SimpleDateFormat("yyyy-MM-dd").format(date)

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormatHour(date: Date): String =
    SimpleDateFormat("HH:mm:ss").format(date)

