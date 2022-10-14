package com.softyorch.taskapp.ui.screensBeta.login.errors

import android.util.Patterns
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import java.util.regex.Pattern

interface WithOutErrorsLogin : IsValidEmail, IsValidPass {

    fun withOutErrorsLogin(loginModel: LoginModel): ErrorLoginModel {
        val errors = ErrorLoginModel()
        !isValidEmail(email = loginModel.userEmail).also { errors.email = !it }
        !isValidPass(pass = loginModel.userPass).also { errors.pass = !it }

        errors.let { it.error = (it.email || it.pass) }

        return errors
    }
}

interface WithOutErrorsNewAccount :
    IsValidName, IsValidEmail, IsValidEmailRepeat, IsValidPass, IsValidPassRepeat {

    fun withOutErrorsNewAccount(
        newAccountModel: NewAccountModel
    ): ErrorNewAccountModel {
        val errors = ErrorNewAccountModel()
        newAccountModel.apply {
            !isValidName(name = userName).also { errors.name = !it }
            !isValidEmail(email = userEmail).also { errors.email = !it }
            !isValidEmail(email = userEmail, emailRepeat = userEmailRepeat).also { errors.emailRepeat = !it }
            !isValidPass(pass = userPass).also { errors.pass = !it }
            !isValidPass(pass = userPass, passRepeat = userPassRepeat).also { errors.passRepeat = !it }
        }

        errors.let {
            it.error = (it.name || it.email || it.emailRepeat || it.pass || it.passRepeat)
        }

        return errors
    }
}

interface IsValidName {
    fun isValidName(name: String): Boolean = (name.length >= 3)
}

interface IsValidEmail {
    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

interface IsValidEmailRepeat {
    fun isValidEmail(email: String, emailRepeat: String): Boolean = (email == emailRepeat)
}

interface IsValidPass {
    fun isValidPass(pass: String): Boolean = Pattern.matches(REGEX_PASSWORD, pass)
}

interface IsValidPassRepeat {
    fun isValidPass(pass: String, passRepeat: String): Boolean = (pass == passRepeat)
}
