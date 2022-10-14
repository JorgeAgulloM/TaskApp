package com.softyorch.taskapp.ui.screensBeta.login.errors

interface IsValidName {
    fun isValidName(name: String): Boolean = (name.length >= 3)
}