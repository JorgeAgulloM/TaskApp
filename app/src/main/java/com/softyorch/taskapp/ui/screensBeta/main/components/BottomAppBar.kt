/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SMALL_TOP_BAR_HEIGHT

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomFakeNavigationBar(
    index: Int,
    items: List<BottomNavItem>,
    settings: Boolean = false,
    show: Boolean = true,
    isEnabled: Boolean = true,
    onItemClick: (BottomNavItem) -> Unit
) {
    //var show by remember { mutableStateOf(value = newTask) }
/*    rememberCoroutineScope().launch {
        delay(100)
        //show = true
    }*/

    BottomMenuBody(show, settings, isEnabled, items, index, onItemClick)
    BottomMenuSettings(show, settings)

}

@Composable
private fun BottomMenuBody(
    show: Boolean,
    newTask: Boolean,
    isEnabled: Boolean,
    items: List<BottomNavItem>,
    index: Int,
    onItemClick: (BottomNavItem) -> Unit
) {
    AnimatedVisibility(
        visible = show && !newTask,
        enter = expandVertically(
            animationSpec = tween(durationMillis = 300),
            expandFrom = Alignment.Top
        ) + fadeIn(
            animationSpec = tween(durationMillis = 300)
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
                            horizontalAlignment = CenterHorizontally
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

@Composable
private fun BottomMenuSettings(show: Boolean, newTask: Boolean) {
    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 5) * 4
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    AnimatedVisibility(
        visible = show && newTask,
        enter = expandVertically(
            animationSpec = tween(durationMillis = 300),
            expandFrom = Alignment.Top
        ) + fadeIn(
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(top = (SMALL_TOP_BAR_HEIGHT + 8).dp)
                .height(calculateHeight.dp)
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large.copy(
                        topStart = CornerSize(3.dp),
                        topEnd = CornerSize(50.dp)
                    )
                )
                .fillMaxSize()
        ) {

        }
    }
}
