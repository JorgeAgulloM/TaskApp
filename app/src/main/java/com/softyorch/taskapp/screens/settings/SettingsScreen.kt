package com.softyorch.taskapp.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.softyorch.taskapp.model.Preferences
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.TaskSwitch
import com.softyorch.taskapp.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, preferencesViewModel: PreferencesViewModel) {
    Hello("Settings")
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Settings",
                icon = Icons.Rounded.Settings,
                nameScreen = TaskAppScreens.SettingsScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, preferencesViewModel = preferencesViewModel)
        })
}

@Composable
private fun Content(it: PaddingValues, preferencesViewModel: PreferencesViewModel) {
    var preferences = preferencesViewModel.preferencesList.collectAsState().value
    var hideLightDark by rememberSaveable{ mutableStateOf(preferences[0].lightDarkAutomaticTheme)}

    if (preferences.isEmpty()) {
        val newPreferences = Preferences(
            id = 0,
            lightDarkAutomaticTheme = true,
            lightOrDarkTheme = false,
            automaticLanguage = true,
            automaticColors = false,
            preferenceBooleanFive = false
        )
        preferencesViewModel.insertPreferences(
            preferences = newPreferences
        )
        preferences = preferencesViewModel.preferencesList.collectAsState().value
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        TaskSwitch(
            "Light/Dark Automatic Theme",
            checked = preferences[0].lightDarkAutomaticTheme,
            onCheckedChange = {
                preferences[0].lightDarkAutomaticTheme = !preferences[0].lightDarkAutomaticTheme
                preferencesViewModel.updatePreferences(preferences[0])
                hideLightDark = !hideLightDark
            }
        )
        if (!hideLightDark) {
            TaskSwitch(
                "Manual light/dark theme",
                checked = preferences[0].lightOrDarkTheme,
                enable = !preferences[0].lightDarkAutomaticTheme,
                onCheckedChange = {
                    preferences[0].lightOrDarkTheme = !preferences[0].lightOrDarkTheme
                    preferencesViewModel.updatePreferences(preferences[0])
                }
            )
        }
        TaskSwitch(
            "Automatic language",
            checked = preferences[0].automaticLanguage,
            onCheckedChange = {
                preferences[0].automaticLanguage = !preferences[0].automaticLanguage
                preferencesViewModel.updatePreferences(preferences[0])
            }
        )
        TaskSwitch(
            "Automatic colors",
            checked = preferences[0].automaticColors,
            onCheckedChange = {
                preferences[0].automaticColors = !preferences[0].automaticColors
                preferencesViewModel.updatePreferences(preferences[0])
            }
        )
        TaskSwitch(
            "No se que añadir aquí",
            checked = preferences[0].preferenceBooleanFive,
            onCheckedChange = {
                preferences[0].preferenceBooleanFive = !preferences[0].preferenceBooleanFive
                preferencesViewModel.updatePreferences(preferences[0])
            }
        )
    }
}

private fun InitPreferences(preferences: Preferences) {

}