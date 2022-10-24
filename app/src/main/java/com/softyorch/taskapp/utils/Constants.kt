package com.softyorch.taskapp.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.regex.Pattern.*

private const val ELEVATION: Int = 2
val ELEVATION_DP: Dp = ELEVATION.dp
const val ELEVATION_FLOAT: Float = (ELEVATION).toFloat()

val KEYBOARD_OPTIONS_CUSTOM: KeyboardOptions = KeyboardOptions.Default.copy(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrect = false,
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
const val SMALL_TOP_BAR_HEIGHT = 35

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
const val USER_DATA = "user_data"

/******* Animations ************************/

const val TIME_IN_MILLIS_OF_DELAY: Long = 100
const val DURATION_MILLIS_BTN_CHANGE_COLOR: Int = 300

@OptIn(ExperimentalAnimationApi::class)
//val ANIMATED_ENTER: EnterTransition = scaleIn(animationSpec = tween(500))
val ANIMATED_ENTER: EnterTransition = expandHorizontally(expandFrom = Alignment.Start)

//val ENTER_SCALE_IN_TWEEN_500: EnterTransition = scaleIn(animationSpec = tween(500))
@OptIn(ExperimentalAnimationApi::class)
//val ANIMATED_EXIT: ExitTransition = scaleOut(animationSpec = tween(500))
val ANIMATED_EXIT: ExitTransition = shrinkHorizontally(shrinkTowards = Alignment.End)
//val EXIT_SCALE_OUT_TWEEN_500: ExitTransition = scaleOut(animationSpec = tween(500))

val ANIMATED_ENTER_TEXT_FIELDS: EnterTransition =
//fadeIn(animationSpec = tween(5000))
//slideInHorizontally(animationSpec = tween(500)) +
//expandHorizontally(animationSpec = tween(500),expandFrom = Alignment.End)
    //slideInVertically(animationSpec = tween(1000)) +
    expandVertically(
        animationSpec = tween(200, 100, easing = LinearEasing),
        expandFrom = Alignment.Bottom
    )
val ANIMATED_EXIT_TEXT_FIELDS: ExitTransition =
    //slideOutHorizontally(animationSpec = tween(500))
    shrinkVertically(
        animationSpec = tween(200, 100, easing = LinearEasing),
        shrinkTowards = Alignment.Top
    )

val SHEET_TRANSITION_ENTER: EnterTransition =
    expandVertically(
        animationSpec = tween( 500, 0, easing = FastOutSlowInEasing),
        expandFrom = Alignment.Bottom
    )

val SHEET_TRANSITION_EXIT: ExitTransition =
    shrinkVertically(
        animationSpec = tween(500, 0, easing = FastOutSlowInEasing),
        shrinkTowards = Alignment.Top
    )

val CARD_RIGHT_ENTER: EnterTransition =
    expandHorizontally(
        animationSpec = tween(durationMillis = 500,0, easing = FastOutSlowInEasing),
        expandFrom = Alignment.End
    )

val CARD_LEFT_EXIT: ExitTransition =
    shrinkHorizontally(
        animationSpec = tween(durationMillis = 500,0, easing = FastOutSlowInEasing),
        shrinkTowards = Alignment.Start
    )


/*enter = if (newTask) slideInVertically {
            with(density) { -500.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Bottom
        ) + fadeIn(
            initialAlpha = 0f
        ) else slideInHorizontally {
            with(density) { 500.dp.roundToPx() }
        } + expandHorizontally (
            expandFrom = Alignment.End
        ) + fadeIn(
            initialAlpha = 0f
        )*/


















