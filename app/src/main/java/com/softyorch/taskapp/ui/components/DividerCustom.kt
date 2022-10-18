/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DividerCustom() {
    Divider(modifier = Modifier.padding(8.dp))
}

@Composable
fun DividerCustom(vertical: Dp) {
    Divider(modifier = Modifier.padding(vertical))
}

@Composable
fun DividerCustom(vertical: Dp = 0.dp, horizontal: Dp = 0.dp) {
    Divider(modifier = Modifier.padding(vertical, horizontal))
}

@Composable
fun DividerCustom(top: Dp = 0.dp, bottom: Dp = 0.dp, start: Dp = 0.dp, end: Dp = 0.dp) {
    Divider(modifier = Modifier.padding(top, bottom, start, end))
}