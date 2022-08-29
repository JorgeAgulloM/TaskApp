package com.softyorch.taskapp.utils

import java.util.regex.Pattern.*

const val weekMillis: Long = 604800000L

val RegexPassword: String = compile(
    """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}${'$'}"""
).pattern()