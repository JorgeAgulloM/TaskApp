package com.softyorch.taskapp.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.ELEVATION_DP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LogoUserCapitalLetter(capitalLetter: String, onTimer: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch(Dispatchers.Default) {
        delay(10000)
        onTimer.invoke()
    }

    Text(
        modifier = Modifier.size(62.dp, 30.dp)
            .padding(horizontal = 16.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            ),
        text = capitalLetter,
        style = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.tertiaryContainer
    )
}