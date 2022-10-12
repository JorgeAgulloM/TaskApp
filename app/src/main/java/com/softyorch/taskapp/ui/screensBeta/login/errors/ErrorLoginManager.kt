package com.softyorch.taskapp.ui.screensBeta.login.errors

import android.util.Patterns
import com.softyorch.taskapp.ui.errors.ErrorUserInput
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import java.util.regex.Pattern

interface ErrorLoginManager {
    private fun isValidName(name: String): Boolean = (name.length >= 3)

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidEmail(email: String, emailRepeat: String): Boolean = (email == emailRepeat)

    private fun isValidPass(pass: String): Boolean = Pattern.matches(REGEX_PASSWORD, pass)

    private fun isValidPass(pass: String, passRepeat: String): Boolean = (pass == passRepeat)

    fun withOutErrors(loginModel: LoginModel): ErrorLoginModel {
        val errors = ErrorLoginModel()
        !isValidEmail(email = loginModel.userEmail).also { errors.email = !it }
        !isValidPass(pass = loginModel.userPass).also { errors.pass = !it }

        errors.let { it.error = (it.email || it.pass) }

        return errors
    }

    fun withOutErrors(
        name: String, email: String, emailRepeat: String, pass: String, passRepeat: String
    ): ErrorNewAccountModel {
        val errors = ErrorNewAccountModel()
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