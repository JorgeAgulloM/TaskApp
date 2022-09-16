package com.softyorch.taskapp.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.PixelCopy
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.datastore.preferences.preferencesDataStore
import java.text.SimpleDateFormat
import java.time.Instant
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

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date? =
    SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).parse(this)

val Context.datastore by preferencesDataStore(name = USER_DATA)

fun Context.toastError(message: String, onShow: () -> Unit) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show().also { onShow() }
}

/** Animations ***************************************************/

@Composable
fun Boolean.alphaAnimation(): State<Float> =
    animateFloatAsState(
        targetValue = if (this) 0.2f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing
        )
    )

@Composable
fun Boolean.containerColorAnimation(): State<Color> =
    animateColorAsState(
        targetValue =
        if (this) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.background,
        animationSpec = tween(
            durationMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing)
    )

@Composable
fun Boolean.intOffsetAnimation(finishedListener: () -> Unit?): State<IntOffset> = animateIntOffsetAsState(
    targetValue = if (this) IntOffset(0, 0)
    else IntOffset(1500, 0),
    animationSpec = tween(
        durationMillis = 400,
        delayMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
        easing = FastOutSlowInEasing
    ),
    finishedListener = { finishedListener() }
)