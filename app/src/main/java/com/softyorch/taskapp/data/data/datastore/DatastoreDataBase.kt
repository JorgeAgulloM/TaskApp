package com.softyorch.taskapp.data.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.utils.NameOfSettings.*
import com.softyorch.taskapp.utils.datastore
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.toDate
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreDataBase @Inject constructor(private val context: Context) {

    suspend fun savaData(userData: UserData) {
        context.datastore.edit { setting ->
            setting[stringPreferencesKey(Name.name)] = userData.username
            setting[stringPreferencesKey(Email.name)] = userData.userEmail
            setting[stringPreferencesKey(Pass.name)] = userData.userPass
            setting[stringPreferencesKey(Picture.name)] = userData.userPicture
            setting[stringPreferencesKey(LastLoginDate.name)] = userData.lastLoginDate.toString()
            setting[booleanPreferencesKey(RememberMe.name)] = userData.rememberMe
            setting[booleanPreferencesKey(LightDarkAutomaticTheme.name)] =
                userData.lightDarkAutomaticTheme
            setting[booleanPreferencesKey(LightOrDarkTheme.name)] = userData.lightOrDarkTheme
            setting[booleanPreferencesKey(AutomaticLanguage.name)] = userData.automaticLanguage
            setting[booleanPreferencesKey(AutomaticColors.name)] = userData.automaticColors
            setting[intPreferencesKey(TimeLimitAutoLoading.name)] = userData.timeLimitAutoLoading
            setting[intPreferencesKey(TextSize.name)] = userData.textSize
        }
    }

    suspend fun deleteData() {
        context.datastore.edit { setting ->
            setting[stringPreferencesKey(Name.name)] = emptyString
            setting[stringPreferencesKey(Email.name)] = emptyString
            setting[stringPreferencesKey(Pass.name)] = emptyString
            setting[stringPreferencesKey(Picture.name)] = emptyString
            setting[stringPreferencesKey(LastLoginDate.name)] = emptyString
            setting[booleanPreferencesKey(RememberMe.name)] = false
            setting[booleanPreferencesKey(LightDarkAutomaticTheme.name)] = false
            setting[booleanPreferencesKey(LightOrDarkTheme.name)] = false
            setting[booleanPreferencesKey(AutomaticLanguage.name)] = false
            setting[booleanPreferencesKey(AutomaticColors.name)] = false
            setting[intPreferencesKey(TimeLimitAutoLoading.name)] = 1
            setting[intPreferencesKey(TextSize.name)] = 0
        }
    }

    fun getData() = context.datastore.data.map { setting ->
        UserData(
            username = setting[stringPreferencesKey(Name.name)].orEmpty(),
            userEmail = setting[stringPreferencesKey(Email.name)].orEmpty(),
            userPass = setting[stringPreferencesKey(Pass.name)].orEmpty(),
            userPicture = setting[stringPreferencesKey(Picture.name)].orEmpty(),
            lastLoginDate = setting[stringPreferencesKey(LastLoginDate.name)]?.toDate(),
            rememberMe = setting[booleanPreferencesKey(RememberMe.name)] ?: false,
            lightDarkAutomaticTheme = setting[booleanPreferencesKey(LightDarkAutomaticTheme.name)]
                ?: false,
            lightOrDarkTheme = setting[booleanPreferencesKey(LightOrDarkTheme.name)] ?: false,
            automaticLanguage = setting[booleanPreferencesKey(AutomaticLanguage.name)] ?: false,
            automaticColors = setting[booleanPreferencesKey(AutomaticColors.name)] ?: false,
            timeLimitAutoLoading = setting[intPreferencesKey(TimeLimitAutoLoading.name)] ?: 0,
            textSize = setting[intPreferencesKey(TextSize.name)] ?: 0
        )
    }
}