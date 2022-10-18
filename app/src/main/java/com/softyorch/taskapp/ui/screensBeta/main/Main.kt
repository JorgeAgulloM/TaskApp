/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBeta(navController: NavController) {
    var index by remember { mutableStateOf(value = 0) }

    Scaffold(
        topBar = {},
        floatingActionButton = { FABCustom() },
        bottomBar = {

            BottomFakeNavigationBar(
                index = index,
                items = BottomNavItem.items,
                navController = navController,
                onItemClick = { itemButton ->
                    BottomNavItem.items.forEach { item ->
                        if (item.indexId == itemButton.indexId)
                            index = item.indexId
                    }
                }
            )
        }
    ) { Text(text = index.toString()) }
}

data class BottomNavItem(
    var indexId: Int,
    val name: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
) {
    companion object{
        val items by remember {
            mutableStateOf(
                value = listOf(
                    BottomNavItem(
                        0, "To do", Icons.Rounded.CheckBoxOutlineBlank, 0
                    ),
                    BottomNavItem(
                        1, "Do it", Icons.Rounded.CheckBox, 5
                    ),
                    BottomNavItem(
                        2, "History", Icons.Rounded.History, 10
                    )
                )
            )
        }
    }
}