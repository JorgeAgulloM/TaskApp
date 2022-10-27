/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.components.SpacerCustom
import com.softyorch.taskapp.ui.components.SwitchCustom
import com.softyorch.taskapp.utils.sdk29AndUp
import com.softyorch.taskapp.utils.sdk31AndUp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SettingsContent() {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val manualThemeShow: Boolean by viewModel.manualThemeShow.observeAsState(initial = false)
    val settings by viewModel.settings.observeAsState()

    if (settings != null) {
        var visible by rememberSaveable { mutableStateOf(settings!!.rememberMe) }
        SpacerCustom(bottom = 24.dp)
        SwitchCustom(
            text = stringResource(R.string.light_dark_automatic_theme),
            checked = settings!!.lightDarkAutomaticTheme,
            enable = !isLoading && sdk29AndUp,
            description = if (sdk29AndUp) stringResource(R.string.settings_app_adapts_theme)
            else stringResource(R.string.settings_only_android_10)
        ) {
            viewModel.let { vm ->
                vm.applyChanges(
                    settings!!.copy(lightDarkAutomaticTheme = it)
                )
                vm.manualShow(!it)
            }
        }

        if (manualThemeShow) SwitchCustom(
            text = stringResource(R.string.manual_light_dark),
            checked = settings!!.lightOrDarkTheme,
            enable = !isLoading,
            description = stringResource(R.string.settings_switch_light_dark)
        ) {
            viewModel.applyChanges(
                settings!!.copy(lightOrDarkTheme = it)
            )
        }

        /*SwitchCustom(
            text = stringResource(automatic_language),
            checked = settings.automaticLanguage,
            enable = enabled,
            description = stringResource(settings_default_language_or_choose)
        ) {
            settings.automaticLanguage = !settings.automaticLanguage
            enabled = !enabled
            viewModel.applyChanges()
        }*/

        SwitchCustom(
            text = stringResource(R.string.automatic_colors),
            checked = settings!!.automaticColors,
            enable = !isLoading && sdk31AndUp,
            description = if (sdk31AndUp) stringResource(R.string.settings_app_color_adapt)
            else stringResource(R.string.settings_only_android_12)
        ) {
            viewModel.applyChanges(
                settings!!.copy(automaticColors = it)
            )
        }

        SwitchCustom(
            text = stringResource(R.string.remember_me),
            checked = settings!!.rememberMe,
            enable = !isLoading,
            description = stringResource(R.string.settings_autologin_limit_time)
        ) {
            visible = !visible
            viewModel.viewModelScope.launch {
                delay(1000)
                viewModel.applyChanges(
                    settings!!.copy(rememberMe = it)
                )
            }
        }

        AnimatedBlock(visible = visible, settings = settings!!, isLoading = !isLoading) {
            viewModel.applyChanges(
                settings!!.copy(timeLimitAutoLoading = it)
            )
        }

    }

    if (isLoading)
        CircularIndicatorCustom(
            text = stringResource(R.string.loading_loading)
        )
}