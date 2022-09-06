package com.softyorch.taskapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DevelopScreen() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("Primaries")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.primary)){ Text(text = "primary")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.primaryContainer)){ Text(text = "primaryContainer")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onPrimary)){ Text(text = "onPrimary")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onPrimaryContainer)){ Text(text = "onPrimaryContainer")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.inversePrimary)){ Text(text = "inversePrimary")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("Secondaries")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.secondary)){ Text(text = "secondary")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.secondaryContainer)){ Text(text = "secondaryContainer")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onSecondary)){ Text(text = "onSecondary")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onSecondaryContainer)){ Text(text = "onSecondaryContainer")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("Terciaries")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.tertiary)){ Text(text = "tertiary")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.tertiaryContainer)){ Text(text = "tertiaryContainer")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onTertiary)){ Text(text = "onTertiary")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onTertiaryContainer)){ Text(text = "onTertiaryContainer")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("Surfaces")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.surface)){ Text(text = "surface")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.surfaceTint)){ Text(text = "surfaceTint")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.surfaceVariant)){ Text(text = "surfaceVariant")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onSurface)){ Text(text = "onSurface")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.inverseOnSurface)){ Text(text = "inverseOnSurface")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.inverseSurface)){ Text(text = "inverseSurface")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onSurfaceVariant)){ Text(text = "onSurfaceVariant")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("Backgrounds")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.background)){ Text(text = "background")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onBackground)){ Text(text = "onBackground")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("errors")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.error)){ Text(text = "error")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.errorContainer)){ Text(text = "errorContainer")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onError)){ Text(text = "onError")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.onErrorContainer)){ Text(text = "onErrorContainer")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("outLine")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.outline)){ Text(text = "outline")}
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.outlineVariant)){ Text(text = "outlineVariant")}
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text("scrim")
        Box(modifier = Modifier.size(200.dp, 35.dp).background(color = MaterialTheme.colorScheme.scrim)){ Text(text = "scrim")}
    }
}