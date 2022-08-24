package com.softyorch.taskapp.utils

sealed class TimeLimitAutoLogin {
    data class OneDay(val time: Long = 604800000L / 7): TimeLimitAutoLogin()
    data class OneWeek(val time: Long = 604800000L): TimeLimitAutoLogin()
    data class TwoWeeks(val time: Long = 604800000L * 2): TimeLimitAutoLogin()
    data class OneMonth(val time: Long = 604800000L * 4): TimeLimitAutoLogin()
    data class SixMonth(val time: Long = 604800000L * 28): TimeLimitAutoLogin()
    data class OneYear(val time: Long = 604800000L * 56): TimeLimitAutoLogin()
}
