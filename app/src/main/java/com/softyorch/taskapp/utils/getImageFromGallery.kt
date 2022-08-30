package com.softyorch.taskapp.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.LocalServerSocket
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import java.io.File

object ImageController {
    fun selectPhotoFromGallery(activity: Activity, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImage(context: Context, id: Long, uri: Uri) {
        val file = File(context.filesDir, id.toString())
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)
    }

    fun getImageUri(context: Context, id: Long): Uri {
        val file = File(context.filesDir, id.toString())
        return if (file.exists()) Uri.fromFile(file)
        else Uri.parse("app/src/main/res/drawable/notes_512x512.png" )
    }

    fun deleteImage(context: Context, id: Long) {
        val file = File(context.filesDir, id.toString())
        file.delete()
    }
}