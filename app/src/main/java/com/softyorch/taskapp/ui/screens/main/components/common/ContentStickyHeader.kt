/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.utils.ELEVATION_DP

@Composable
fun ContentStickyHeader(published: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.text_create_at) + published,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(horizontal = 8.dp)
                .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.large
                ),
            style = MaterialTheme.typography.labelSmall
                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center
        )
    }
    Divider(modifier = Modifier.padding(start = 16.dp,end = 48.dp), thickness = 1.5.dp)
}