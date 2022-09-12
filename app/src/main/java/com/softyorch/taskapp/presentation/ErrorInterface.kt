package com.softyorch.taskapp.presentation

import android.util.Log
import android.util.Patterns
import com.softyorch.taskapp.presentation.ErrorUserInput.*
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

    fun withOutErrors(email: String, pass: String): Error {
        val errors = Error()
        !isValidEmail(email = email).also { errors.email = !it }
        !isValidPass(pass = pass).also { errors.pass = !it }
        Log.d("ERRORS", "withOutErrors.errors.email -> ${errors.email}")
        Log.d("ERRORS", "withOutErrors.errors.pass -> ${errors.pass}")
        errors.let { it.error = (it.email || it.pass) }

        return errors
    }

    fun withOutErrors(name: String, email: String, pass: String): Error {
        val errors = Error()
        !isValidName(name = name).also { errors.name = !it }
        !isValidEmail(email = email).also { errors.email = !it }
        !isValidPass(pass = pass).also { errors.pass = !it }

        errors.let { it.error = (it.name || it.email || it.pass) }

        return errors
    }

    fun withOutErrors(
        name: String, email: String, emailRepeat: String, pass: String, passRepeat: String
    ): Error {
        val errors = Error()
        !isValidName(name = name).also { errors.name = !it }
        !isValidEmail(email = email).also { errors.email = !it }
        !isValidEmail(email = email, emailRepeat = emailRepeat).also { errors.emailRepeat = !it }
        !isValidPass(pass = pass).also { errors.pass = !it }
        !isValidPass(pass = pass, passRepeat = passRepeat).also { errors.passRepeat = !it }

        errors.let {
            it.error = (it.name || it.email || it.emailRepeat || it.pass || it.passRepeat)
        }

        return errors
    }
}