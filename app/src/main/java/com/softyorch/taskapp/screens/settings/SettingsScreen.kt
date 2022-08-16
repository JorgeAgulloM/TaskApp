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
import com.softyorch.taskapp.model.Settings
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.TaskSwitch
import com.softyorch.taskapp.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    Hello("Settings")
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Settings",
                icon = Icons.Rounded.Settings,
                nameScreen = AppScreens.SettingsScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, settingsViewModel = settingsViewModel)
        })
}

@Composable
private fun Content(it: PaddingValues, settingsViewModel: SettingsViewModel) {
    val settings = settingsViewModel.settingsList.collectAsState().value
    var hideLightDark by rememberSaveable{ mutableStateOf(settings[0].lightDarkAutomaticTheme)}

    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        TaskSwitch(
            "Light/Dark Automatic Theme",
            checked = settings[0].lightDarkAutomaticTheme,
            onCheckedChange = {
                settings[0].lightDarkAutomaticTheme = !settings[0].lightDarkAutomaticTheme
                settingsViewModel.updatePreferences(settings[0])
                hideLightDark = !hideLightDark
            }
        )
        if (!hideLightDark) {
            TaskSwitch(
                "Manual light/dark theme",
                checked = settings[0].lightOrDarkTheme,
                enable = !settings[0].lightDarkAutomaticTheme,
                onCheckedChange = {
                    settings[0].lightOrDarkTheme = !settings[0].lightOrDarkTheme
                    settingsViewModel.updatePreferences(settings[0])
                }
            )
        }
        TaskSwitch(
            "Automatic language",
            checked = settings[0].automaticLanguage,
            onCheckedChange = {
                settings[0].automaticLanguage = !settings[0].automaticLanguage
                settingsViewModel.updatePreferences(settings[0])
            }
        )
        TaskSwitch(
            "Automatic colors",
            checked = settings[0].automaticColors,
            onCheckedChange = {
                settings[0].automaticColors = !settings[0].automaticColors
                settingsViewModel.updatePreferences(settings[0])
            }
        )
        TaskSwitch(
            "No se que añadir aquí",
            checked = settings[0].preferenceBooleanFive,
            onCheckedChange = {
                settings[0].preferenceBooleanFive = !settings[0].preferenceBooleanFive
                settingsViewModel.updatePreferences(settings[0])
            }
        )
    }
}