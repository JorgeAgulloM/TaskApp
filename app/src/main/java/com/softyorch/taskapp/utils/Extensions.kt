package com.softyorch.taskapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
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

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date? =
    SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).parse(this)

val Context.datastore by preferencesDataStore(name = USER_DATA)

fun Context.toastError(message: String, onShow: () -> Unit) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show().also { onShow() }
}

/** Animations ***************************************************/

/*@Composable
fun Boolean.alphaAnimation(): State<Float> =
    animateFloatAsState(
        targetValue = if (this) 0.2f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing
        )
    )*/

@Composable
fun Boolean.alphaAnimation(
    stateOne: Boolean = true, stateTwo: Boolean = true, finishedListener: () -> Unit?
): State<Float> =
    animateFloatAsState(
        targetValue = if ((stateOne || stateTwo) && this) 0.2f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { finishedListener() }
    )

@Composable
fun Boolean.containerColorAnimation(): State<Color> =
    animateColorAsState(
        targetValue =
        if (this) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.background,
        animationSpec = tween(
            durationMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing
        )
    )

@Composable
fun Boolean.containerColorAnimation(finishedListener: () -> Unit?): State<Color> =
    animateColorAsState(
        targetValue =
        if (this) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.onBackground,
        animationSpec = tween(
            durationMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { finishedListener() }
    )

@Composable
fun Boolean.intOffsetAnimation(stateOne: Boolean): State<IntOffset> =
    animateIntOffsetAsState(
        targetValue = if (stateOne && this) IntOffset(60, 0)
        else IntOffset(0, 0),
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100,
            easing = LinearEasing
        )
    )

@Composable
fun Boolean.intOffsetAnimationTransition(
    finishedListener: () -> Unit?
): State<IntOffset> =
    animateIntOffsetAsState(
        targetValue = if (this) IntOffset(0, 0)
        else IntOffset(1500, 0),
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = DURATION_MILLIS_BTN_CHANGE_COLOR,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { finishedListener() }
    )

@Composable
fun Boolean.contentColorAsStateAnimation(stateOne: Boolean, stateTwo: Boolean):
        State<Color> = animateColorAsState(
    targetValue =
    if (this) MaterialTheme.colorScheme.onError
    else if (stateOne) MaterialTheme.colorScheme.background
    else if (stateTwo) MaterialTheme.colorScheme.onTertiary
    else MaterialTheme.colorScheme.onBackground,
    animationSpec = tween(durationMillis = DURATION_MILLIS_BTN_CHANGE_COLOR)
)

@Composable
fun Boolean.containerColorAsStateAnimation(
    stateOne: Boolean, stateTwo: Boolean, stateThree: Boolean, finishListener: () -> Unit?
):
        State<Color> = animateColorAsState(
    targetValue =
    if (this) MaterialTheme.colorScheme.error
    else if (stateOne) MaterialTheme.colorScheme.primaryContainer
    else if (stateTwo) MaterialTheme.colorScheme.primary
    else if (stateThree) MaterialTheme.colorScheme.tertiary
    else Color.Transparent,
    animationSpec = tween(durationMillis = DURATION_MILLIS_BTN_CHANGE_COLOR),
    finishedListener = { finishListener() }
)

@Composable
fun MaterialTheme.infiniteTransitionAnimateColor(): State<Color> =
    rememberInfiniteTransition().animateColor(
        initialValue = this.colorScheme.tertiary,
        targetValue = this.colorScheme.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )















