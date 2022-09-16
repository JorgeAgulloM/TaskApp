package com.softyorch.taskapp.presentation.screens.settings

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.components.switchCustom.SwitchCustom
import com.softyorch.taskapp.presentation.components.sliderCustom.sliderCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.presentation.widgets.RowInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
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
        }
    ) {
        Content(it = it, reloadComposable = reloadComposable)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun Content(it: PaddingValues, reloadComposable: () -> Unit) {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val settings = viewModel.settings.observeAsState().value
    val needReload: Boolean by viewModel.needReload.observeAsState(initial = false)
    var enabled: Boolean by remember { mutableStateOf(value = true) }

    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {

        if (settings != null) {
            var visible by rememberSaveable { mutableStateOf(settings.rememberMe) }

            SwitchCustomSettings(
                text = stringResource(light_dark_automatic_theme),
                checked = settings.lightDarkAutomaticTheme,
                enabled = enabled
            ) {
                settings.lightDarkAutomaticTheme = !settings.lightDarkAutomaticTheme
                enabled = !enabled
                viewModel.applyChanges()
            }

            if (!settings.lightDarkAutomaticTheme) SwitchCustomSettings(
                text = stringResource(manual_light_dark),
                checked = settings.lightOrDarkTheme,
                enabled = enabled
            ) {
                settings.lightOrDarkTheme = !settings.lightOrDarkTheme
                enabled = !enabled
                viewModel.applyChanges()
            }


            SwitchCustomSettings(
                text = stringResource(automatic_language),
                checked = settings.automaticLanguage,
                enabled = enabled
            ) {
                settings.automaticLanguage = !settings.automaticLanguage
                enabled = !enabled
                viewModel.applyChanges()
            }

            SwitchCustomSettings(
                text = stringResource(automatic_colors),
                checked = settings.automaticColors,
                enabled = enabled
            ) {
                settings.automaticColors = !settings.automaticColors
                enabled = !enabled
                viewModel.applyChanges()
            }

            SwitchCustomSettings(
                text = stringResource(remember_me),
                checked = settings.rememberMe,
                enabled = enabled
            ) {
                settings.rememberMe = !settings.rememberMe
                visible = !visible
                enabled = !enabled
                viewModel.viewModelScope.launch {
                    delay(1000)
                    viewModel.applyChanges()
                }
            }

            AnimatedBlock(visible = visible, settings = settings, enabled = enabled) {
                enabled = !enabled
                viewModel.applyChanges()
            }

            if (isLoading || !enabled)
                CircularIndicatorCustom(
                    text = stringResource(loading_loading),
                    modifier = Modifier.padding(top = 16.dp).safeContentPadding().fillMaxWidth()
                )

            if (needReload) {
                reloadComposable()
                viewModel.reloaded()
            }
        }
    }
}

@Composable
private fun ColumnScope.AnimatedBlock(
    visible: Boolean,
    settings: UserData,
    enabled: Boolean,
    funcOfViewModel: () -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Column() {
            Row(modifier = Modifier.padding(start = 32.dp, top = 16.dp),
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

            settings.timeLimitAutoLoading =
                sliderCustomSettingsAutoLoading(
                    initValue = settings.timeLimitAutoLoading,
                    enabled = enabled
                ) {
                    funcOfViewModel()
                }
        }
    }
}

@Composable
private fun SwitchCustomSettings(
    text: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: () -> Unit
) = SwitchCustom(
    text = text,
    checked = checked,
    enable = enabled,
    onCheckedChange = { onCheckedChange() }
)

@Composable
private fun sliderCustomSettingsAutoLoading(
    initValue: Int,
    enabled: Boolean,
    onValueChangeFinished: () -> Unit
): Int {
    return sliderCustom(
        initValue = initValue,
        enable = enabled,
        text = stringResource(time_automatic_login) + " " + timeLimitAutoLoginSelectText(initValue),
        onValueChangeFinished = { onValueChangeFinished() }
    )
}

