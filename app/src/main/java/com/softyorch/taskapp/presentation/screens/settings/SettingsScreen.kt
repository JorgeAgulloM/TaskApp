package com.softyorch.taskapp.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.components.switchCustom.SwitchCustom
import com.softyorch.taskapp.presentation.components.sliderCustom.sliderCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.presentation.widgets.RowInfo
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

@Composable
private fun Content(it: PaddingValues, reloadComposable: () -> Unit) {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val settings = viewModel.settings.observeAsState().value
    val reloading: Boolean by viewModel.reloading.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        if (isLoading) CircularIndicatorCustom(text = "...loading")
        if (settings != null) {

            SwitchCustomSettings(
                text = "Light/Dark Automatic Theme",
                checked = settings.lightDarkAutomaticTheme
            ) {
                settings.lightDarkAutomaticTheme = !settings.lightDarkAutomaticTheme
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            if (!settings.lightDarkAutomaticTheme) SwitchCustomSettings(
                text = "Manual light/dark theme",
                checked = settings.lightOrDarkTheme
            ) {
                settings.lightOrDarkTheme = !settings.lightOrDarkTheme
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            SwitchCustomSettings(
                text = "Automatic language",
                checked = settings.automaticLanguage
            ) {
                settings.automaticLanguage = !settings.automaticLanguage
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            SwitchCustomSettings(
                text = "Automatic colors",
                checked = settings.automaticColors
            ) {
                settings.automaticColors = !settings.automaticColors
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            SwitchCustomSettings(
                text = "Remember Me",
                checked = settings.rememberMe
            ) {
                settings.rememberMe = !settings.rememberMe
                coroutineScope.launch { viewModel.applyChanges() }
            }

            if (settings.rememberMe) Row(modifier = Modifier.padding(start = 40.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                content = {
                    Icon(Icons.Rounded.Info, contentDescription = "Last time to manual login")
                    RowInfo(
                        text = "Last login manual: ${
                            settings.lastLoginDate?.toStringFormatted()
                        }",
                        paddingStart = 8.dp
                    )
                }
            )

            if (settings.rememberMe) settings.timeLimitAutoLoading =
                sliderCustomSettingsAutoLoading(
                    initValue = settings.timeLimitAutoLoading, needReloadDialog = reloading
                ) {
                    coroutineScope.launch { viewModel.applyChanges() }
                }
        }
    }
}

@Composable
private fun SwitchCustomSettings(
    text: String,
    checked: Boolean,
    onCheckedChange: () -> Unit
) = SwitchCustom(
    text = text,
    checked = checked,
    enable = true,
    onCheckedChange = { onCheckedChange() }
)

@Composable
private fun sliderCustomSettingsAutoLoading(
    initValue: Int,
    needReloadDialog: Boolean,
    onValueChangeFinished: () -> Unit
): Int {
    return sliderCustom(
        initValue = initValue,
        enable = !needReloadDialog,
        onValueChangeFinished = { onValueChangeFinished() },
        text = "Time to automatic login: ${timeLimitAutoLoginSelectText(initValue)}"
    )
}

