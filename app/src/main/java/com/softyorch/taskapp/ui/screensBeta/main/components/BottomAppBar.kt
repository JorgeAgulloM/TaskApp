/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.screensBeta.main.BottomNavItem
import com.softyorch.taskapp.utils.ELEVATION_DP


@Composable
fun BottomFakeNavigationBar(
    index: Int,
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {

    BottomNavigation (
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.onBackground,
        //cutoutShape = MaterialTheme.shapes.large,
        elevation = ELEVATION_DP
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                selected = item.indexId == index,
                onClick = { onItemClick(item) },
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
                                        color = MaterialTheme.colorScheme.error
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
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}