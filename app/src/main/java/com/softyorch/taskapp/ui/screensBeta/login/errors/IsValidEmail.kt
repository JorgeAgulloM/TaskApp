package com.softyorch.taskapp.ui.screensBeta.login.errors

import android.util.Patterns

interface IsValidEmail {
    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}