package com.softyorch.taskapp.ui.screensBeta.login.errors

interface IsValidEmailRepeat {
    fun isValidEmail(email: String, emailRepeat: String): Boolean = (email == emailRepeat)
}