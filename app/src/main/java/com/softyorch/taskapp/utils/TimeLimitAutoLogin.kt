package com.softyorch.taskapp.utils

import androidx.compose.ui.platform.LocalContext
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.utils.TimeLimitAutoLogin.*

sealed class TimeLimitAutoLogin {
    data class OneDay(
        val time: Long = WEEK_IN_MILLIS / 7, val textTime: String =
            LocalContext.current.getString(one_day)
    ) : TimeLimitAutoLogin()

    data class OneWeek(
        val time: Long = WEEK_IN_MILLIS, val textTime: String = LocalContext.current.getString(one_week)
    ) : TimeLimitAutoLogin()

    data class TwoWeeks(
        val time: Long = WEEK_IN_MILLIS * 2, val textTime: String = LocalContext.current.getString(two_weeks)
    ) : TimeLimitAutoLogin()

    data class OneMonth(
        val time: Long = WEEK_IN_MILLIS * 4, val textTime: String = LocalContext.current.getString(one_month)
    ) : TimeLimitAutoLogin()

    data class SixMonth(
        val time: Long = WEEK_IN_MILLIS * 28, val textTime: String = LocalContext.current.getString(six_months)
    ) : TimeLimitAutoLogin()

    data class OneYear(
        val time: Long = WEEK_IN_MILLIS * 56, val textTime: String = LocalContext.current.getString(one_year)
    ) : TimeLimitAutoLogin()
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

fun timeLimitAutoLoginSelectText(selector: Int): String =
    when (selector) {
        0 -> OneDay().textTime
        1 -> OneWeek().textTime
        2 -> TwoWeeks().textTime
        3 -> OneMonth().textTime
        4 -> SixMonth().textTime
        5 -> OneYear().textTime
        else -> {
            OneWeek().textTime
        }
    }