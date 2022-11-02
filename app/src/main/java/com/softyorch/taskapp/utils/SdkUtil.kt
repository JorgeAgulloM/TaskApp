package com.softyorch.taskapp.utils

import android.os.Build

inline fun <T> sdk33AndUp(onSdk33: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) onSdk33() else null

inline fun <T> sdk32AndUp(onSdk32: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) onSdk32() else null

inline fun <T> sdk31AndUp(onSdk31: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) onSdk31() else null

inline fun <T> sdk30AndUp(onSdk30: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) onSdk30() else null

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) onSdk29() else null

inline fun <T> sdk28AndUp(onSdk28: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) onSdk28() else null

inline fun <T> sdk27AndUp(onSdk27: () -> T): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) onSdk27() else null

val sdk33AndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
val sdk32AndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2
val sdk31AndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
val sdk30ndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
val sdk29AndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
val sdk28AndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
val sdk27AndUp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1