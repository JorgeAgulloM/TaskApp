package com.softyorch.taskapp.ui.screensBeta.login.errors

interface IsValidPassRepeat {
    fun isValidPass(pass: String, passRepeat: String): Boolean = (pass == passRepeat)
}
