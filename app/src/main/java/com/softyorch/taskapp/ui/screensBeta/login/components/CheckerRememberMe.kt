package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.CheckCustom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckerRememberMe(
    rememberMe: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    CheckCustom(
        checked = rememberMe,
        onCheckedChange = onCheckedChange,
        text = stringResource(R.string.remember_me),
        horizontalArrangement = Arrangement.End,
        onClick = {}
    )
}