package com.softyorch.taskapp.presentation.screens.settings

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewModelScope
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
    val settings = viewModel.settings.observeAsState().value
    val reloading: Boolean by viewModel.reloading.observeAsState(initial = false)
    val textSizes = viewModel.sizeSelectedOfUser()


    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        if (settings != null) {

            SwitchCustomSettings(
                text = "Light/Dark Automatic Theme",
                checked = settings.lightDarkAutomaticTheme,
                enable = !reloading
            ) {
                settings.lightDarkAutomaticTheme = !settings.lightDarkAutomaticTheme
                ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
            }

            if (!settings.lightDarkAutomaticTheme) SwitchCustomSettings(
                text = "Manual light/dark theme",
                checked = settings.lightOrDarkTheme,
                enable = !reloading
            ) {
                settings.lightOrDarkTheme = !settings.lightOrDarkTheme
                ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
            }

            SwitchCustomSettings(
                text = "Automatic language",
                checked = settings.automaticLanguage,
                enable = !reloading
            ) {
                settings.automaticLanguage = !settings.automaticLanguage
                ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
            }

            SwitchCustomSettings(
                text = "Automatic colors",
                checked = settings.automaticColors,
                enable = !reloading
            ) {
                settings.automaticColors = !settings.automaticColors
                ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
            }

            SwitchCustomSettings(
                text = "Remember Me",
                checked = settings.rememberMe,
                enable = !reloading
            ) {
                settings.rememberMe = !settings.rememberMe
                ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
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
                        paddingStart = 8.dp,
                        textSizes = textSizes
                    )
                }
            )

            if (settings.rememberMe) settings.timeLimitAutoLoading =
                sliderCustomSettingsAutoLoading(
                    initValue = settings.timeLimitAutoLoading, needReloadDialog = reloading
                ) {
                    ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
                }

            settings.textSize = sliderCustomSettingsText(
                initValue = settings.textSize, needReloadDialog = reloading
            ) {
                ApplyChanges(viewModel = viewModel, reloadComposable = reloadComposable)
            }

            if (reloading) CircularIndicatorCustom(text = "Realizando los cambios")

        }
    }
}

@Composable
fun SwitchCustomSettings(
    text: String,
    checked: Boolean,
    enable: Boolean,
    onCheckedChange: () -> Unit
) = SwitchCustom(
    text = text,
    checked = checked,
    onCheckedChange = { onCheckedChange() },
    enable = enable
)

@Composable
private fun sliderCustomSettingsText(
    initValue: Int,
    needReloadDialog: Boolean,
    onValueChangeFinished: () -> Unit
): Int {
    return sliderCustom(
        initValue = initValue,
        enable = !needReloadDialog,
        valueRange = 0f..4f,
        steps = 3,
        onValueChangeFinished = { onValueChangeFinished() },
        text = "Text base size: ${sizeTextName(initValue)}"
    )
}

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

@SuppressLint("CoroutineCreationDuringComposition")
private fun ApplyChanges(viewModel: SettingsViewModel, reloadComposable: () -> Unit){
    viewModel.viewModelScope.launch {
        viewModel.applyChanges()
        reloadComposable.invoke()
    }
}

