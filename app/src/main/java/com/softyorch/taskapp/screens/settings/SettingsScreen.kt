package com.softyorch.taskapp.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.components.SwitchCustom
import com.softyorch.taskapp.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.navigation.AppScreens

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "Settings",
                nameScreen = AppScreens.SettingsScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it)
        })
}

@Composable
private fun Content(it: PaddingValues) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    var settingsUserData = viewModel.getUserActiveSharedPreferences()

    var hideLightDark by rememberSaveable {
        mutableStateOf(
            settingsUserData?.lightDarkAutomaticTheme ?: false
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        if (settingsUserData != null) {
            SwitchCustom(
                "Light/Dark Automatic Theme",
                checked = settingsUserData.lightDarkAutomaticTheme,
                onCheckedChange = {
                    settingsUserData.lightDarkAutomaticTheme =
                        !settingsUserData.lightDarkAutomaticTheme
                    hideLightDark = !hideLightDark
                    viewModel.updatePreferences(settingsUserData = settingsUserData)
                }
            )

            if (!hideLightDark) {

                SwitchCustom(
                    "Manual light/dark theme",
                    checked = settingsUserData.lightOrDarkTheme,
                    onCheckedChange = {
                        settingsUserData.lightOrDarkTheme = !settingsUserData.lightOrDarkTheme
                        viewModel.updatePreferences(settingsUserData = settingsUserData)
                    }
                )

            }

            SwitchCustom(
                "Automatic language",
                checked = settingsUserData.automaticLanguage,
                onCheckedChange = {
                    settingsUserData.automaticLanguage = !settingsUserData.automaticLanguage
                    viewModel.updatePreferences(settingsUserData = settingsUserData)
                }
            )


            SwitchCustom(
                "Automatic colors",
                checked = settingsUserData.automaticColors,
                onCheckedChange = {
                    settingsUserData.automaticColors = !settingsUserData.automaticColors
                    viewModel.updatePreferences(settingsUserData = settingsUserData)
                }
            )


            SwitchCustom(
                "Remember Me",
                checked = settingsUserData.rememberMe,
                onCheckedChange = {
                    settingsUserData.rememberMe = !settingsUserData.rememberMe
                    viewModel.updatePreferences(settingsUserData = settingsUserData)
                }
            )
        }
    }
}