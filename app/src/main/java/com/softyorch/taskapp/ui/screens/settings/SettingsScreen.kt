package com.softyorch.taskapp.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.components.SwitchCustom
import com.softyorch.taskapp.ui.components.sliderCustom.sliderCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.ui.widgets.RowInfo
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
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = scrollState)
        .padding(top = it.calculateTopPadding() * 1.5f)) {

        if (settings != null) {
            var visible by rememberSaveable { mutableStateOf(settings.rememberMe) }

            SwitchCustomSettings(
                text = stringResource(light_dark_automatic_theme),
                checked = settings.lightDarkAutomaticTheme,
                enabled = enabled && viewModel.minSdk29,
                description = if (viewModel.minSdk29) stringResource(settings_app_adapts_theme)
                else stringResource(settings_only_android_10)
            ) {
                settings.lightDarkAutomaticTheme = !settings.lightDarkAutomaticTheme
                enabled = !enabled
                viewModel.applyChanges()
            }

            if (!settings.lightDarkAutomaticTheme) SwitchCustomSettings(
                text = stringResource(manual_light_dark),
                checked = settings.lightOrDarkTheme,
                enabled = enabled,
                description = stringResource(settings_switch_light_dark)
            ) {
                settings.lightOrDarkTheme = !settings.lightOrDarkTheme
                enabled = !enabled
                viewModel.applyChanges()
            }


            /*SwitchCustomSettings(
                text = stringResource(automatic_language),
                checked = settings.automaticLanguage,
                enabled = enabled,
                description = stringResource(settings_default_language_or_choose)
            ) {
                settings.automaticLanguage = !settings.automaticLanguage
                enabled = !enabled
                viewModel.applyChanges()
            }*/

            SwitchCustomSettings(
                text = stringResource(automatic_colors),
                checked = settings.automaticColors,
                enabled = enabled && viewModel.minSdk31,
                description = if (viewModel.minSdk31) stringResource(settings_app_color_adapt)
                else stringResource(settings_only_android_12)
            ) {
                settings.automaticColors = !settings.automaticColors
                enabled = !enabled
                viewModel.applyChanges()
            }

            SwitchCustomSettings(
                text = stringResource(remember_me),
                checked = settings.rememberMe,
                enabled = enabled,
                description = stringResource(settings_autologin_limit_time)
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
    settings: UserDataEntity,
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
    description: String,
    onCheckedChange: () -> Unit
) = SwitchCustom(
    text = text,
    checked = checked,
    enable = enabled,
    description = description,
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
        text = stringResource(time_automatic_login) + " " + getString(initValue),
        onValueChangeFinished = { onValueChangeFinished() }
    )
}

@Composable
private fun getString(needString: Int): String =
    when (needString) {
        0 -> stringResource(limit_time_one_day)
        1 -> stringResource(limit_time_one_week)
        2 -> stringResource(limit_time_two_week)
        3 -> stringResource(limit_time_one_month)
        4 -> stringResource(limit_time_six_month)
        5 -> stringResource(limit_time_one_year)
        else -> stringResource(unknown)
    }

