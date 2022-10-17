/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import android.util.Log
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
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomAppBarCustom

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBeta(navController: NavController) {
    var check by remember { mutableStateOf(value = 0) }


    Scaffold(
        topBar = {},
        floatingActionButton = { FABCustom() },
        bottomBar = {
            val items by remember {
                mutableStateOf(
                    value = listOf(
                        BottomNavItem(
                            true, "UnChecked Items", Icons.Rounded.CheckBoxOutlineBlank, 0
                        ),
                        BottomNavItem(
                            false, "Checked Items", Icons.Rounded.CheckBox, 5
                        ),
                        BottomNavItem(
                            false, "History", Icons.Rounded.History, 10
                        )
                    )
                )
            }
            BottomAppBarCustom(
                items = items,
                navController = navController,
                onItemClick = { itemButton ->
                    items.forEach { item ->
                        item.selected = (item.name == itemButton.name)
                        item.badgeCount += 1
                    }
                    Log.d("ITEMS", "Items -> $items")
                    if (check < 2) {
                        check += 1
                    } else {
                        check = 0
                    }
                }
            )
        }
    ) { Text(text = check.toString()) }
}

sealed class ItemNav() {
    object Checked : ItemNav()
    object UnChecked : ItemNav()
    object History : ItemNav()
}

data class BottomNavItem(
    var selected: Boolean,
    val name: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
)