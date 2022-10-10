package com.softyorch.taskapp.ui.components

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
        modifier = Modifier.fillMaxWidth().padding(top = 0.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = stringResource(R.string.text_create_at) + published,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(horizontal = 8.dp)
                .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.large
                ),
            style = MaterialTheme.typography.labelSmall
                .copy(color = MaterialTheme.colorScheme.secondary),
            textAlign = TextAlign.Center
        )
    }
    Divider(modifier = Modifier.padding(start = 48.dp,end = 16.dp), thickness = 0.5.dp)
}