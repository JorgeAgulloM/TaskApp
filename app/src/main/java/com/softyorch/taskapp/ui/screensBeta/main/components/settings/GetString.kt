/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R

@Composable
fun getString(needString: Int): String =
    when (needString) {
        0 -> stringResource(R.string.limit_time_one_day)
        1 -> stringResource(R.string.limit_time_one_week)
        2 -> stringResource(R.string.limit_time_two_week)
        3 -> stringResource(R.string.limit_time_one_month)
        4 -> stringResource(R.string.limit_time_six_month)
        5 -> stringResource(R.string.limit_time_one_year)
        else -> stringResource(R.string.unknown)
    }