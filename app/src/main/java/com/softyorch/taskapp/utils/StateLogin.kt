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
            sp?.putString("name", name)
            sp?.putString("pass", pass)
            sp?.putBoolean("activate", activate)
            sp?.putString("last_login_date", lastLoginDate.toString())
            sp?.putBoolean("remember_me", rememberMe)
            sp?.putBoolean("light_dark_automatic_theme", lightDarkAutomaticTheme)
            sp?.putBoolean("light_or_dark_theme", lightOrDarkTheme)
            sp?.putBoolean("automatic_language", automaticLanguage)
            sp?.putBoolean("automatic_colors", automaticColors)
            sp?.putLong("time_limit_auto_loading", timeLimitAutoLoading)
            sp?.putInt("text_size", textSize)

            sp?.apply()
        }
    }

    fun userActive(): UserData {
        _sharedPreferences.let { sp ->
            return UserData(
                username = sp?.getString("name", "").toString(),
                userEmail = "",
                userPass = sp?.getString("pass", "").toString(),
                rememberMe = sp?.getBoolean("remember_me", false) == true
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

        _sharedPreferences?.let { sp ->
            if (!sp.getString("name", "").isNullOrEmpty()) {
                list.addAll(getSharedPreferences())
            } else {
                sp.edit().let { spe ->
                    list.add(spe.putString("last_login_date", Date.from(Instant.now()).toString()))
                    list.add(spe.putBoolean("remember_me", false))
                    list.add(spe.putBoolean("light_dark_automatic_theme", true))
                    list.add(spe.putBoolean("light_or_dark_theme", false))
                    list.add(spe.putBoolean("automatic_language", true))
                    list.add(spe.putBoolean("automatic_colors", false))
                    list.add(spe.putLong("time_limit_auto_loading", 604800000L))
                    list.add(spe.putInt("text_size", 0))

                    spe.apply()
                }
            }
        }
        return list
    }

    fun getSharedPreferences(): List<Any> {
        val list: MutableList<Any> = mutableListOf()
        _sharedPreferences?.let { sp ->
            list.add(sp.getString("last_login_date", Date.from(Instant.now()).toString())!!)
            list.add(sp.getBoolean("remember_me", false))
            list.add(sp.getBoolean("light_dark_automatic_theme", true))
            list.add(sp.getBoolean("light_or_dark_theme", false))
            list.add(sp.getBoolean("automatic_language", true))
            list.add(sp.getBoolean("automatic_colors", false))
            list.add(sp.getLong("time_limit_auto_loading", 604800000L))
            list.add(sp.getInt("text_size", 0))
        }
        return list
    }
}