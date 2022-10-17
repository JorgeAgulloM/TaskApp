/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.errors

interface IsValidPassRepeat {
    fun isValidPass(pass: String, passRepeat: String): Boolean = (pass == passRepeat)
}
