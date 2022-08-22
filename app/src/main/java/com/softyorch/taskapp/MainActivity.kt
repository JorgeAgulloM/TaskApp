package com.softyorch.taskapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softyorch.taskapp.navigation.TaskAppNavigation
import com.softyorch.taskapp.ui.theme.TaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            TaskApp(sharedPreferences = sharedPreferences)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun TaskApp(sharedPreferences: SharedPreferences) {
    //val settingList = listOf(true, false, true, false, false)
    val settingList = loadSettings(sharedPreferences = sharedPreferences)

    TaskAppTheme(
        darkTheme = if (!settingList[0]) settingList[1] else isSystemInDarkTheme(),
        dynamicColor = settingList[2]
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TaskAppNavigation(sharedPreferences = sharedPreferences)
        }
    }
}

private fun loadSettings(sharedPreferences: SharedPreferences): List<Boolean> {
    val settings: MutableList<Boolean> = mutableListOf(false, false, false, false, false, false)
    sharedPreferences.let { sp ->
        if (!sp.getString("name", "").isNullOrEmpty()) {
            settings[0] = sp.getBoolean("bool_pref_one", true)
            settings[1] = sp.getBoolean("bool_pref_two", false)
            settings[2] = sp.getBoolean("bool_pref_three", true)
            settings[3] = sp.getBoolean("bool_pref_four", false)
            settings[4] = sp.getBoolean("bool_pref_five", false)
        } else {
            sp.edit().let { edit ->
                edit.putBoolean("bool_pref_one", true)
                edit.putBoolean("bool_pref_two", false)
                edit.putBoolean("bool_pref_three", true)
                edit.putBoolean("bool_pref_four", false)
                edit.putBoolean("bool_pref_five", false)

                edit.apply()
            }
        }

    }
    return settings
}
