package com.softyorch.taskapp.utils

import android.content.SharedPreferences
import com.softyorch.taskapp.model.UserData
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
        sharedPreferencesSetUser(
            name = userData.username,
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

    fun logOut() {
        sharedPreferencesSetUser()
    }

    private fun sharedPreferencesSetUser(
        name: String = "",
        pass: String = "",
        activate: Boolean = false,
        lastLoginDate: Date? = null,
        rememberMe: Boolean = false,
        lightDarkAutomaticTheme: Boolean = true,
        lightOrDarkTheme: Boolean = false,
        automaticLanguage: Boolean = true,
        automaticColors: Boolean = false,
        timeLimitAutoLoading: Long = 604800000L, //One week
        textSize: Int = 0
    ) {
        _sharedPreferences?.edit().let { sp ->
            sp?.putString(Settings.Name.name, name)
            sp?.putString(Settings.Pass.name, pass)
            sp?.putBoolean(Settings.Activate.name, activate)
            sp?.putString(Settings.LastLoginDate.name, lastLoginDate.toString())
            sp?.putBoolean(Settings.RememberMe.name, rememberMe)
            sp?.putBoolean(Settings.LightDarkAutomaticTheme.name, lightDarkAutomaticTheme)
            sp?.putBoolean(Settings.LightOrDarkTheme.name, lightOrDarkTheme)
            sp?.putBoolean(Settings.AutomaticLanguage.name, automaticLanguage)
            sp?.putBoolean(Settings.AutomaticColors.name, automaticColors)
            sp?.putLong(Settings.TimeLimitAutoLoading.name, timeLimitAutoLoading)
            sp?.putInt(Settings.TextSize.name, textSize)

            sp?.apply()
        }
    }

    fun userActive(): UserData {
        _sharedPreferences!!.let { sp ->
            return UserData(
                username = sp.getString(Settings.Name.name, "").toString(),
                userEmail = "",
                userPass = sp.getString(Settings.Pass.name, "").toString(),
                rememberMe = sp.getBoolean(Settings.RememberMe.name, false)
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
        if (!_sharedPreferences?.getString(Settings.Name.name, "").isNullOrEmpty()) {
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
            spe.putString(Settings.LastLoginDate.name, Date.from(Instant.now()).toString())
            spe.putBoolean(Settings.RememberMe.name, false)
            spe.putBoolean(Settings.LightDarkAutomaticTheme.name, true)
            spe.putBoolean(Settings.LightOrDarkTheme.name, false)
            spe.putBoolean(Settings.AutomaticLanguage.name, true)
            spe.putBoolean(Settings.AutomaticColors.name, false)
            spe.putLong(Settings.TimeLimitAutoLoading.name, 604800000L)
            spe.putInt(Settings.TextSize.name, 0)

            spe.apply()
        }
        return getSharedPreferences()
    }

    private fun getSharedPreferences(): List<Any> {
        val list: MutableList<Any> = mutableListOf()
        _sharedPreferences?.let { sp ->
            list.add(
                sp.getString(
                    Settings.LastLoginDate.name,
                    Date.from(Instant.now()).toString()
                )!!
            )
            list.add(sp.getBoolean(Settings.RememberMe.name, false))
            list.add(sp.getBoolean(Settings.LightDarkAutomaticTheme.name, true))
            list.add(sp.getBoolean(Settings.LightOrDarkTheme.name, false))
            list.add(sp.getBoolean(Settings.AutomaticLanguage.name, true))
            list.add(sp.getBoolean(Settings.AutomaticColors.name, false))
            list.add(sp.getLong(Settings.TimeLimitAutoLoading.name, 604800000L))
            list.add(sp.getInt(Settings.TextSize.name, 0))
        }
        return list
    }
}
