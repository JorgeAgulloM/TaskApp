package com.softyorch.taskapp.utils

import com.softyorch.taskapp.utils.TimeLimitAutoLogin.*

sealed class TimeLimitAutoLogin {
    data class OneDay(
        val time: Long = weekMillis / 7, val textTime: String = "One day"
    ) : TimeLimitAutoLogin()

    data class OneWeek(
        val time: Long = weekMillis, val textTime: String = "One week"
    ) : TimeLimitAutoLogin()

    data class TwoWeeks(
        val time: Long = weekMillis * 2, val textTime: String = "Two week"
    ) : TimeLimitAutoLogin()

    data class OneMonth(
        val time: Long = weekMillis * 4, val textTime: String = "One month"
    ) : TimeLimitAutoLogin()

    data class SixMonth(
        val time: Long = weekMillis * 28, val textTime: String = "Six months"
    ) : TimeLimitAutoLogin()

    data class OneYear( val time: Long = weekMillis * 56, val textTime: String = "One year"
    ) : TimeLimitAutoLogin()
}

fun timeLimitAutoLoginSelectTime(selector: Int): Long =
    when (selector){
        0 -> OneDay().time
        1 -> OneWeek().time
        2 -> TwoWeeks().time
        3 -> OneMonth().time
        4 -> SixMonth().time
        5 -> OneYear().time
        else -> { weekMillis }
    }

fun timeLimitAutoLoginSelectText(selector: Int): String =
    when (selector) {
        0 -> OneDay().textTime
        1 -> OneWeek().textTime
        2 -> TwoWeeks().textTime
        3 -> OneMonth().textTime
        4 -> SixMonth().textTime
        5 -> OneYear().textTime
        else -> { OneWeek().textTime }
    }