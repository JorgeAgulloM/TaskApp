package com.softyorch.taskapp.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
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
                title = stringResource(settings),
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
        if (isLoading) CircularIndicatorCustom(text = stringResource(loading_loading))
        if (settings != null) {

            SwitchCustomSettings(
                text = stringResource(light_dark_automatic_theme),
                checked = settings.lightDarkAutomaticTheme
            ) {
                settings.lightDarkAutomaticTheme = !settings.lightDarkAutomaticTheme
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            if (!settings.lightDarkAutomaticTheme) SwitchCustomSettings(
                text = stringResource(manual_light_dark),
                checked = settings.lightOrDarkTheme
            ) {
                settings.lightOrDarkTheme = !settings.lightOrDarkTheme
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            SwitchCustomSettings(
                text = stringResource(automatic_language),
                checked = settings.automaticLanguage
            ) {
                settings.automaticLanguage = !settings.automaticLanguage
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            SwitchCustomSettings(
                text = stringResource(automatic_colors),
                checked = settings.automaticColors
            ) {
                settings.automaticColors = !settings.automaticColors
                coroutineScope.launch {
                    viewModel.applyChanges()
                    reloadComposable()
                }
            }

            SwitchCustomSettings(
                text = stringResource(remember_me),
                checked = settings.rememberMe
            ) {
                settings.rememberMe = !settings.rememberMe
                coroutineScope.launch { viewModel.applyChanges() }
            }

            if (settings.rememberMe) Row(modifier = Modifier.padding(start = 32.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                content = {
                    Icon(
                        Icons.Rounded.Info,
                        contentDescription = stringResource(content_last_time_login),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    RowInfo(
                        text = stringResource(last_login_manual) + " " +
                                settings.lastLoginDate?.toStringFormatted(),
                        paddingStart = 8.dp,
                        style = MaterialTheme.typography.labelSmall
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
        text = stringResource(time_automatic_login) + " " + timeLimitAutoLoginSelectText(initValue),
        onValueChangeFinished = { onValueChangeFinished() }
    )
}

