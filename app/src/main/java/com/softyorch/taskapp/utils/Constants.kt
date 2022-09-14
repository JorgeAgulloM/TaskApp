package com.softyorch.taskapp.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.regex.Pattern.*

private const val ELEVATION: Int = 4
val ELEVATION_DP: Dp = ELEVATION.dp
const val ELEVATION_FLOAT: Float = (ELEVATION / 2).toFloat()

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

/****** others *****************************/
const val emptyString = ""
const val ID = "id"
const val GALLERY_IMAGES = "image/*"

/****** Tables *****************************/
const val TASK_TBL = "tasks_tbl"
const val USERDATA_TBL = "userdata_tbl"

/** Tables -> Names of values -> tasks */
const val TITLE = "task_title"
const val DESCRIPTION = "task_description"
const val AUTHOR = "task_author"
const val ENTRY_DATE = "task_entry_date"
const val FINISH_DATE = "task_finish_date"
const val CHECK_STATE = "task_check_state"

/** Tables -> Names of values -> user_data */
const val USER_NAME = "user_name"
const val USER_EMAIL = "user_email"
const val USER_PASS = "user_pass"
const val USER_PICTURE = "user_picture"
const val LAST_LOGIN = "last_login_date"
const val REMEMBER_ME = "remember_me"
const val LIGHT_DARK_AUTOMATIC = "light_dark_automatic_theme"
const val LIGHT_DARK = "light_or_dark_theme"
const val AUTOMATIC_LANGUAGE = "automatic_language"
const val AUTOMATIC_COLORS = "automatic_colors"
const val TIME_LIMIT_AUTOLOGIN = "time_limit_auto_loading"
const val TEXT_SIZE = "text_size"

/** Tables -> Names of values -> shared preferences */
const val USER_DATA="user_data"

/******* Animations ************************/
@OptIn(ExperimentalAnimationApi::class)
val ENTER_SCALE_IN_TWEEN_500: EnterTransition = scaleIn(animationSpec = tween(500))
@OptIn(ExperimentalAnimationApi::class)
val EXIT_SCALE_OUT_TWEEN_500: ExitTransition = scaleOut(animationSpec = tween(500))