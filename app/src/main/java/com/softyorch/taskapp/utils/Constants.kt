package com.softyorch.taskapp.utils

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.regex.Pattern.*

private const val ELEVATION: Int = 4
val ELEVATION_DP: Dp = ELEVATION.dp
const val ELEVATION_FLOAT: Float = ELEVATION.toFloat()

val KEYBOARD_OPTIONS_CUSTOM: KeyboardOptions = KeyboardOptions.Default.copy(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrect = true,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Next
)

const val WEEK_IN_MILLIS: Long = 604800000L

val REGEX_PASSWORD: String = compile(
    """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}${'$'}"""
).pattern()