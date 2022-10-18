/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBeta(navController: NavController) {
    var index by remember { mutableStateOf(value = 0) }

    Scaffold(
        topBar = { SmallTopAppBarCustom(true, emptyString, navController) },
        bottomBar = {
            BottomFakeNavigationBar(
                index = index,
                items = BottomNavItem.items,
                onItemClick = { itemButton ->
                    BottomNavItem.items.forEach { item ->
                        if (item.indexId == itemButton.indexId)
                            index = item.indexId
                    }
                }
            )
        },
        floatingActionButton = { FABCustom() },
        floatingActionButtonPosition = FabPosition.End,
        contentColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
    ) { Body(it, index) }
}

@Composable
fun Body(paddingValues: PaddingValues, index: Int) {
    val toDoSheetVisibility = index == 0
    val finishedSheetVisibility = index == 1
    val historyVisibility = index == 2
    val contentBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.onTertiary
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = contentBrush, shape = MaterialTheme.shapes.large
            )
            .padding(
                start = 4.dp,
                end = 4.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomSheetCustom(paddingValues, toDoSheetVisibility, "toDo")
        BottomSheetCustom(paddingValues, finishedSheetVisibility, "finished")
        BottomSheetCustom(paddingValues, historyVisibility, "history")
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomSheetCustom(paddingValues: PaddingValues, isVisible: Boolean, text: String) {

    var visible by remember { mutableStateOf(value = false) }
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    rememberCoroutineScope().launch {
        delay(200)
        visible = isVisible
    }
    AnimatedVisibility(
        visible = visible,
        enter = SHEET_TRANSITION_ENTER,
        exit = SHEET_TRANSITION_EXIT
    ) {
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large
                )
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = text)
            }
        }
    }
}


data class BottomNavItem(
    var indexId: Int,
    val name: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
) {
    companion object {
        val items = listOf(
            BottomNavItem(
                0, "To do", Icons.Rounded.CheckBoxOutlineBlank, 0
            ),
            BottomNavItem(
                1, "Finished", Icons.Rounded.CheckBox, 5
            ),
            BottomNavItem(
                2, "History", Icons.Rounded.History, 10
            )
        )
    }
}