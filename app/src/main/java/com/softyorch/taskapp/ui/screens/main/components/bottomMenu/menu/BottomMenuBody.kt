/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.screens.main.components.bottomMenu.BottomNavItem
import com.softyorch.taskapp.utils.ELEVATION_DP

@Composable
fun BottomMenuBody(
    show: Boolean,
    isEnabled: Boolean,
    items: List<BottomNavItem>,
    index: Int,
    onItemClick: (BottomNavItem) -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = expandVertically(
            animationSpec = tween(durationMillis = 300),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Bottom
        )
    ) {
        BottomAppBar(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            ).shadow(
                elevation = ELEVATION_DP * 2, shape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            ),
            backgroundColor = MaterialTheme.colorScheme.background,
            cutoutShape = MaterialTheme.shapes.large,
            elevation = ELEVATION_DP * 2
        ) {
            items.forEach { item ->
                val selected = item.indexId == index
                val gradiant = Brush.verticalGradient(
                    if (selected) listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                    else
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                )
                BottomNavigationItem(
                    selected = selected,
                    onClick = { if (isEnabled) onItemClick(item) },
                    icon = {
                        Column(
                            modifier = Modifier.padding(vertical = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (item.badgeCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            color = if (isEnabled) MaterialTheme.colorScheme.error
                                            else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name
                                    )
                                }
                            } else {
                                Icon(imageVector = item.icon, contentDescription = item.name)
                            }
                            if (item.indexId == index) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(2.dp)
                        .background(
                            brush = gradiant,
                            shape = MaterialTheme.shapes.large
                        ),
                    selectedContentColor = if (isEnabled) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.outline,
                    unselectedContentColor = if (isEnabled) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}