package com.softyorch.taskapp.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.softyorch.taskapp.components.CircularIndicatorCustom
import com.softyorch.taskapp.components.SwitchCustom
import com.softyorch.taskapp.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.utils.toStringFormatted
import com.softyorch.taskapp.widgets.RowInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(navController: NavHostController, changeTheme: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "Settings",
                nameScreen = AppScreens.SettingsScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, changeTheme = changeTheme)
        })
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun Content(it: PaddingValues, changeTheme: () -> Unit) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val settingsUserData = viewModel.getUserActiveSharedPreferences()
    var needReloadDialog by remember { mutableStateOf(false) }

    var hideLightDark by rememberSaveable {
        mutableStateOf(
            settingsUserData?.lightDarkAutomaticTheme ?: false
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        if (settingsUserData != null) {

            RowInfo(
                text = "Last login manual: ${settingsUserData.lastLoginDate?.toStringFormatted(
                    settingsUserData.lastLoginDate!!
                )}",
                fontSize = 12.sp,
                paddingStart = 16.dp
            )

            SwitchCustom(
                "Light/Dark Automatic Theme",
                checked = settingsUserData.lightDarkAutomaticTheme,
                onCheckedChange = {
                    settingsUserData.lightDarkAutomaticTheme =
                        !settingsUserData.lightDarkAutomaticTheme
                    hideLightDark = !hideLightDark
                    needReloadDialog = true
                },
                enable = !needReloadDialog
            )

            if (!hideLightDark) SwitchCustom(
                "Manual light/dark theme",
                checked = settingsUserData.lightOrDarkTheme,
                onCheckedChange = {
                    settingsUserData.lightOrDarkTheme = !settingsUserData.lightOrDarkTheme
                    needReloadDialog = true
                },
                enable = !needReloadDialog
            )

            SwitchCustom(
                "Automatic language",
                checked = settingsUserData.automaticLanguage,
                onCheckedChange = {
                    settingsUserData.automaticLanguage = !settingsUserData.automaticLanguage
                    needReloadDialog = true
                },
                enable = !needReloadDialog
            )

            SwitchCustom(
                "Automatic colors",
                checked = settingsUserData.automaticColors,
                onCheckedChange = {
                    settingsUserData.automaticColors = !settingsUserData.automaticColors
                    needReloadDialog = true
                },
                enable = !needReloadDialog
            )

            SwitchCustom(
                "Remember Me",
                checked = settingsUserData.rememberMe,
                onCheckedChange = {
                    settingsUserData.rememberMe = !settingsUserData.rememberMe
                    viewModel.updatePreferences(settingsUserData = settingsUserData)
                },
                enable = !needReloadDialog
            )

            if (needReloadDialog) {
                ApplyChanges(viewModel, settingsUserData, changeTheme)
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun ApplyChanges(
    viewModel: SettingsViewModel,
    settingsUserData: UserData?,
    changeTheme: () -> Unit
) {
    settingsUserData?.let { viewModel.updatePreferences(settingsUserData = it) }

    var showDialog = true

    if (showDialog) CircularIndicatorCustom(text = "Realizando los cambios")

    viewModel.viewModelScope.launch {
        showDialog = false
        delay(1000)
        changeTheme.invoke()
    }
}