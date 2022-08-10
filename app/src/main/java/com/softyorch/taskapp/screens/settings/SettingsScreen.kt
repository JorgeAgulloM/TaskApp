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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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

    if (preferences.isEmpty()) {
        val newPreferences = Preferences(
            id = 0,
            preferenceBooleanOne = false,
            preferenceBooleanTwo = false,
            preferenceBooleanThree = false,
            preferenceBooleanFour = false,
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
            checked = preferences[0].preferenceBooleanOne
        )
        TaskSwitch(
            "Manual light/dark theme",
            checked = preferences[0].preferenceBooleanTwo
        )
        TaskSwitch(
            "Automatic language",
            checked = preferences[0].preferenceBooleanThree
        )
        TaskSwitch(
            "Automatic colors",
            checked = preferences[0].preferenceBooleanFour
        )
        TaskSwitch(
            "No se que añadir aquí",
            checked = preferences[0].preferenceBooleanFive
        )
    }
}

private fun InitPreferences(preferencesViewModel: PreferencesViewModel) {

}