package com.softyorch.taskapp.utils

import android.content.SharedPreferences
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.utils.NameOfSettings.*
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateLogin @Inject constructor(
) {

    private var _sharedPreferences: SharedPreferences? = null
    var userDataActive: UserData? = null

    fun loadSharedPreferences(sharedPreferences: SharedPreferences) {
        _sharedPreferences = sharedPreferences
    }

    fun logIn(
        userData: UserData
    ) {
        userDataActive = userData
        refreshData(userData = userData)
    }

    fun logOut() {
        sharedPreferencesSetUser()
    }

    fun refreshData(userData: UserData) {
        sharedPreferencesSetUser(
            name = userData.username,
            email = userData.userEmail,
            pass = userData.userPass,
            lastLoginDate = userData.lastLoginDate,
            activate = true,
            rememberMe = userData.rememberMe,
            lightDarkAutomaticTheme = userData.lightDarkAutomaticTheme,
            lightOrDarkTheme = userData.lightOrDarkTheme,
            automaticLanguage = userData.automaticLanguage,
            automaticColors = userData.automaticColors,
            timeLimitAutoLoading = userData.timeLimitAutoLoading,
            textSize = userData.textSize
        )
    }

    private fun sharedPreferencesSetUser(
        name: String = "",
        email: String = "",
        pass: String = "",
        activate: Boolean = false,
        lastLoginDate: Date? = null,
        rememberMe: Boolean = false,
        lightDarkAutomaticTheme: Boolean = true,
        lightOrDarkTheme: Boolean = false,
        automaticLanguage: Boolean = true,
        automaticColors: Boolean = false,
        timeLimitAutoLoading: Int = 1, //One week
        textSize: Int = 0
    ) {
        _sharedPreferences?.edit().let { sp ->
            sp?.putString(Name.name, name)
            sp?.putString(Email.name, email)
            sp?.putString(Pass.name, pass)
            sp?.putBoolean(Activate.name, activate)
            sp?.putString(LastLoginDate.name, lastLoginDate.toString())
            sp?.putBoolean(RememberMe.name, rememberMe)
            sp?.putBoolean(LightDarkAutomaticTheme.name, lightDarkAutomaticTheme)
            sp?.putBoolean(LightOrDarkTheme.name, lightOrDarkTheme)
            sp?.putBoolean(AutomaticLanguage.name, automaticLanguage)
            sp?.putBoolean(AutomaticColors.name, automaticColors)
            sp?.putInt(TimeLimitAutoLoading.name, timeLimitAutoLoading)
            sp?.putInt(TextSize.name, textSize)

            sp?.apply()
        }
    }

    fun userActive(): UserData {
        _sharedPreferences!!.let { sp ->
            return UserData(
                username = sp.getString(Name.name, "").toString(),
                userEmail = sp.getString(Email.name, "").toString(),
                userPass = sp.getString(Pass.name, "").toString(),
                rememberMe = sp.getBoolean(RememberMe.name, false)
            )
        }
    }

    fun isTheUserActive(): Boolean {
        userActive().let { user ->
            return user.rememberMe
        }
    }

    fun loadSettings(): List<Any> {
        val list: MutableList<Any> = mutableListOf()
        if (!_sharedPreferences?.getString(Name.name, "").isNullOrEmpty()) {
            list.addAll(getSharedPreferences())
        } else {
            list.addAll(setSharedPreferencesFirst())
        }
        return list
    }

    /**
     * Is only used the first time the user starts the application
     */
    private fun setSharedPreferencesFirst(): List<Any> {
        _sharedPreferences?.edit()?.let { spe ->
            spe.putString(LastLoginDate.name, Date.from(Instant.now()).toString())
            spe.putBoolean(RememberMe.name, false)
            spe.putBoolean(LightDarkAutomaticTheme.name, true)
            spe.putBoolean(LightOrDarkTheme.name, false)
            spe.putBoolean(AutomaticLanguage.name, true)
            spe.putBoolean(AutomaticColors.name, false)
            spe.putInt(TimeLimitAutoLoading.name, 1)
            spe.putInt(TextSize.name, 0)

            spe.apply()
        }
        return getSharedPreferences()
    }

    private fun getSharedPreferences(): List<Any> {
        val list: MutableList<Any> = mutableListOf()
        _sharedPreferences?.let { sp ->
            list.add(
                sp.getString(
                    LastLoginDate.name,
                    Date.from(Instant.now()).toString()
                )!!
            )
            list.add(sp.getBoolean(RememberMe.name, false))
            list.add(sp.getBoolean(LightDarkAutomaticTheme.name, true))
            list.add(sp.getBoolean(LightOrDarkTheme.name, false))
            list.add(sp.getBoolean(AutomaticLanguage.name, true))
            list.add(sp.getBoolean(AutomaticColors.name, false))
            list.add(sp.getInt(TimeLimitAutoLoading.name, 1))
            list.add(sp.getInt(TextSize.name, 0))
        }
        return list
    }

    /**
     * Si no hay un usuario logeado, el tamaño estandard será el normal configurado como tamaño 2*/
    fun getTextSizeSelectedOfUser(): StandardizedSizes =
        StandardizedSizes(userDataActive?.textSize ?: 2)

}
