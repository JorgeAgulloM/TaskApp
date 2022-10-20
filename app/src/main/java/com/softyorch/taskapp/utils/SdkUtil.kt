package com.softyorch.taskapp.utils

import android.os.Build

inline fun <T> sdk31AndUp(onSdk31: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) onSdk31() else null

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) onSdk29() else null

inline fun <T> sdk28AndUp(onSdk28: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) onSdk28() else null

inline fun <T> sdk27AndUp(onSdk27: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) onSdk27() else null

