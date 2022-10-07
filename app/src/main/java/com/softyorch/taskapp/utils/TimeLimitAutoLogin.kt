package com.softyorch.taskapp.utils

import com.softyorch.taskapp.utils.TimeLimitAutoLogin.*

sealed class TimeLimitAutoLogin {
    data class OneDay(val time: Long = WEEK_IN_MILLIS / 7) : TimeLimitAutoLogin()
    data class OneWeek(val time: Long = WEEK_IN_MILLIS) : TimeLimitAutoLogin()
    data class TwoWeeks(val time: Long = WEEK_IN_MILLIS * 2) : TimeLimitAutoLogin()
    data class OneMonth(val time: Long = WEEK_IN_MILLIS * 4) : TimeLimitAutoLogin()
    data class SixMonth(val time: Long = WEEK_IN_MILLIS * 28) : TimeLimitAutoLogin()
    data class OneYear(val time: Long = WEEK_IN_MILLIS * 56) : TimeLimitAutoLogin()
}

fun timeLimitAutoLoginSelectTime(selector: Int): Long =
    when (selector) {
        0 -> OneDay().time
        1 -> OneWeek().time
        2 -> TwoWeeks().time
        3 -> OneMonth().time
        4 -> SixMonth().time
        5 -> OneYear().time
        else -> {
            WEEK_IN_MILLIS
        }
    }
