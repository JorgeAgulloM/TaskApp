/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.errors

interface IsValidName {
    fun isValidName(name: String): Boolean = (name.length >= 3)
}