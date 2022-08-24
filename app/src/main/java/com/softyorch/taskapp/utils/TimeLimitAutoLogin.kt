package com.softyorch.taskapp.utils

sealed class TimeLimitAutoLogin {
    data class OneDay(
        val time: Long = weekMillis / 7,
        val textTime: String = "One day"
    ) :
        TimeLimitAutoLogin()

    data class OneWeek(
        val time: Long = weekMillis,
        val textTime: String = "One week"
    ) :
        TimeLimitAutoLogin()

    data class TwoWeeks(
        val time: Long = weekMillis * 2,
        val textTime: String = "Two week"
    ) :
        TimeLimitAutoLogin()

    data class OneMonth(
        val time: Long = weekMillis * 4,
        val textTime: String = "One month"
    ) :
        TimeLimitAutoLogin()

    data class SixMonth(
        val time: Long = weekMillis * 28,
        val textTime: String = "Six months"
    ) :
        TimeLimitAutoLogin()

    data class OneYear(
        val time: Long = weekMillis * 56,
        val textTime: String = "One year"
    ) :
        TimeLimitAutoLogin()
}

fun timeLimitAutoLoginSelectTime(selector: Int): Long {
    var timeResult = 0L
    when (selector){
        0 -> TimeLimitAutoLogin.OneDay().let { timeResult = it.time }
        1 -> TimeLimitAutoLogin.OneWeek().let { timeResult = it.time }
        2 -> TimeLimitAutoLogin.TwoWeeks().let { timeResult = it.time }
        3 -> TimeLimitAutoLogin.OneMonth().let { timeResult = it.time }
        4 -> TimeLimitAutoLogin.SixMonth().let { timeResult = it.time }
        5 -> TimeLimitAutoLogin.OneYear().let { timeResult = it.time }
    }
    return timeResult
}

fun timeLimitAutoLoginSelectText(selector: Int): String {
    var textResult = ""
    when (selector) {
        0 -> TimeLimitAutoLogin.OneDay().let { textResult = it.textTime }
        1 -> TimeLimitAutoLogin.OneWeek().let { textResult = it.textTime }
        2 -> TimeLimitAutoLogin.TwoWeeks().let { textResult = it.textTime }
        3 -> TimeLimitAutoLogin.OneMonth().let { textResult = it.textTime }
        4 -> TimeLimitAutoLogin.SixMonth().let { textResult = it.textTime }
        5 -> TimeLimitAutoLogin.OneYear().let { textResult = it.textTime }
    }
    return textResult
}