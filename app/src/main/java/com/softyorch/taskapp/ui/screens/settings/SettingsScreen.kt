package com.softyorch.taskapp.ui.screens.settings

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.components.switchCustom.SwitchCustom
import com.softyorch.taskapp.ui.components.sliderCustom.sliderCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.ui.widgets.RowInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(navController: NavHostController, reloadComposable: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "Settings",
                nameScreen = AppScreens.SettingsScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, reloadComposable = reloadComposable)
        })
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun Content(it: PaddingValues, reloadComposable: () -> Unit) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val settingsUserData = viewModel.getUserActiveSharedPreferences()
    val textSizes = viewModel.sizeSelectedOfUser()
    var needReloadDialog by remember { mutableStateOf(false) }
    var hideLightDark by rememberSaveable {
        mutableStateOf(
            settingsUserData?.lightDarkAutomaticTheme ?: false
        )
    }
    var rememberMe by rememberSaveable {
        mutableStateOf(
            settingsUserData?.rememberMe ?: false
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
                    rememberMe = settingsUserData.rememberMe
                    //viewModel.updatePreferences(settingsUserData = settingsUserData)
                    needReloadDialog = true
                },
                enable = !needReloadDialog
            )

            if (rememberMe) Row(modifier = Modifier.padding(start = 40.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                content = {
                    Icon(Icons.Rounded.Info, contentDescription = "Last time to manual login")
                    RowInfo(
                        text = "Last login manual: ${
                            settingsUserData.lastLoginDate?.toStringFormatted(
                                settingsUserData.lastLoginDate!!
                            )
                        }",
                        paddingStart = 8.dp,
                        textSizes = textSizes
                    )
                }
            )

            if (rememberMe) settingsUserData.timeLimitAutoLoading = sliderCustom(
                initValue = settingsUserData.timeLimitAutoLoading,
                enable = !needReloadDialog,
                onValueChangeFinished = {
                    needReloadDialog = true
                },
                text = "Time to automatic login: ${
                    timeLimitAutoLoginSelectText(
                        settingsUserData.timeLimitAutoLoading
                    )
                }"
            )

            settingsUserData.textSize = sliderCustom(
                initValue = settingsUserData.textSize,
                enable = !needReloadDialog,
                valueRange = 0f..4f,
                steps = 3,
                onValueChangeFinished = {
                    needReloadDialog = true
                },
                text = "Text base size: ${sizeTextName(settingsUserData.textSize)}"
            )

            if (needReloadDialog) {
                ApplyChanges(viewModel, settingsUserData, reloadComposable)
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

/*
private fun loadData(selection: Int){
    when (selection) {
        0 -> TimeLimitAutoLogin.OneDay().let {
            returnedValue = it.time
            textShow = it.textTime
        }
        1 -> TimeLimitAutoLogin.OneWeek().let {
            returnedValue = it.time
            textShow = it.textTime
        }
        2 -> TimeLimitAutoLogin.TwoWeeks().let {
            returnedValue = it.time
            textShow = it.textTime
        }
        3 -> TimeLimitAutoLogin.OneMonth().let {
            returnedValue = it.time
            textShow = it.textTime
        }
        4 -> TimeLimitAutoLogin.SixMonth().let {
            returnedValue = it.time
            textShow = it.textTime
        }
        5 -> TimeLimitAutoLogin.OneYear().let {
            returnedValue = it.time
            textShow = it.textTime
        }
    }
}*/
