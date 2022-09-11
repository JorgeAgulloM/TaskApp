package com.softyorch.taskapp.presentation

import android.util.Patterns
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import java.util.regex.Pattern

interface ErrorInterface {

    fun compareStrings(string1: String, string2: String): Boolean = string1 == string2

    private fun isValidName(name: String): Boolean = (name.length >= 3)

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidEmail(email: String, emailRepeat: String): Boolean = (email == emailRepeat)

    private fun isValidPass(pass: String): Boolean = Pattern.matches(REGEX_PASSWORD, pass)

    private fun isValidPass(pass: String, passRepeat: String): Boolean = (pass == passRepeat)

    fun withOutErrors(email: String, pass: String): Boolean =
        (!isValidEmail(email = email) || !isValidPass(pass = pass))

    fun withOutErrors(
        name: String, email: String, emailRepeat: String, pass: String, passRepeat: String
    ): Boolean = (!isValidName(name = name) || !isValidEmail(email = email) ||
            !isValidEmail(email = email, emailRepeat = emailRepeat) ||
            !isValidPass(pass = pass) || !isValidPass(pass = pass, passRepeat = passRepeat))

}