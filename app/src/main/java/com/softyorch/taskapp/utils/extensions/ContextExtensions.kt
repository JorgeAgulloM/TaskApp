package com.softyorch.taskapp.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import com.softyorch.taskapp.utils.USER_DATA

val Context.datastore by preferencesDataStore(name = USER_DATA)

fun Context.toastError(message: String, onShow: () -> Unit) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show().also { onShow() }
}