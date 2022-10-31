/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors

interface IsValidEmailRepeat {
    fun isValidEmail(email: String, emailRepeat: String): Boolean = (email == emailRepeat)
}