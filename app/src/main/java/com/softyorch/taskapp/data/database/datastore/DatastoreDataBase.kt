package com.softyorch.taskapp.data.database.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.utils.NameOfSettings.*
import com.softyorch.taskapp.utils.datastore
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.toDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreDataBase @Inject constructor(private val context: Context) {

    suspend fun saveData(userDataEntity: UserDataEntity) {
        context.datastore.edit { setting ->
            setting[stringPreferencesKey(Name.name)] = userDataEntity.username
            setting[stringPreferencesKey(Email.name)] = userDataEntity.userEmail
            setting[stringPreferencesKey(Pass.name)] = userDataEntity.userPass
            setting[stringPreferencesKey(Picture.name)] = userDataEntity.userPicture
            setting[stringPreferencesKey(LastLoginDate.name)] = userDataEntity.lastLoginDate.toString()
            setting[booleanPreferencesKey(RememberMe.name)] = userDataEntity.rememberMe
            setting[booleanPreferencesKey(LightDarkAutomaticTheme.name)] =
                userDataEntity.lightDarkAutomaticTheme
            setting[booleanPreferencesKey(LightOrDarkTheme.name)] = userDataEntity.lightOrDarkTheme
            setting[booleanPreferencesKey(AutomaticLanguage.name)] = userDataEntity.automaticLanguage
            setting[booleanPreferencesKey(AutomaticColors.name)] = userDataEntity.automaticColors
            setting[intPreferencesKey(TimeLimitAutoLoading.name)] = userDataEntity.timeLimitAutoLoading
            setting[intPreferencesKey(TextSize.name)] = userDataEntity.textSize
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

    fun getData(): Resource<Flow<UserDataEntity>> {
        return try {
            Resource.Success(data = context.datastore.data.map { setting ->
                UserDataEntity(
                    username = setting[stringPreferencesKey(Name.name)].orEmpty(),
                    userEmail = setting[stringPreferencesKey(Email.name)].orEmpty(),
                    userPass = setting[stringPreferencesKey(Pass.name)].orEmpty(),
                    userPicture = setting[stringPreferencesKey(Picture.name)].orEmpty(),
                    lastLoginDate = if (setting[stringPreferencesKey(LastLoginDate.name)].isNullOrEmpty()) {
                        null
                    } else {
                        if (setting[stringPreferencesKey(LastLoginDate.name)] == "null"){
                            Date.from(Instant.now())
                        } else {
                            setting[stringPreferencesKey(LastLoginDate.name)]?.toDate()
                        }
                    },
                    rememberMe = setting[booleanPreferencesKey(RememberMe.name)] ?: false,
                    lightDarkAutomaticTheme = setting[booleanPreferencesKey(LightDarkAutomaticTheme.name)]
                        ?: false,
                    lightOrDarkTheme = setting[booleanPreferencesKey(LightOrDarkTheme.name)]
                        ?: false,
                    automaticLanguage = setting[booleanPreferencesKey(AutomaticLanguage.name)]
                        ?: false,
                    automaticColors = setting[booleanPreferencesKey(AutomaticColors.name)] ?: false,
                    timeLimitAutoLoading = setting[intPreferencesKey(TimeLimitAutoLoading.name)]
                        ?: 0,
                    textSize = setting[intPreferencesKey(TextSize.name)] ?: 0
                )
            })
        } catch (e: Exception) {
            Resource.Error(data = null, message = e.message.toString())
        }

    }
}