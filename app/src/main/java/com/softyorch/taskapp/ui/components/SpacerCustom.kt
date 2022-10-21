package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpacerCustom() {
    Spacer(modifier = Modifier.padding(8.dp))
}

@Composable
fun SpacerCustom(vertical: Dp) {
    Spacer(modifier = Modifier.padding(vertical))
}

@Composable
fun SpacerCustom(vertical: Dp = 0.dp, horizontal: Dp = 0.dp) {
    Spacer(modifier = Modifier.padding(vertical, horizontal))
}

@Composable
fun SpacerCustom(top: Dp = 0.dp, bottom: Dp = 0.dp, start: Dp = 0.dp, end: Dp = 0.dp) {
    Spacer(modifier = Modifier.padding(top, bottom, start, end))
}

