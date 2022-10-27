/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

import com.softyorch.taskapp.utils.REGEX_PASSWORD
import java.util.regex.Pattern

interface IsValidPass {
    fun isValidPass(pass: String): Boolean = Pattern.matches(REGEX_PASSWORD, pass)
}