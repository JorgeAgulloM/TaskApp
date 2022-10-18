/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBeta(navController: NavController) {
    var index by remember { mutableStateOf(value = 0) }
    val items = BottomNavItem.items

    Scaffold(
        topBar = { SmallTopAppBarCustom(true, items[index].name, navController) },
        bottomBar = {
            BottomFakeNavigationBar(
                index = index,
                items = items,
                onItemClick = { itemButton ->
                    items.forEach { item ->
                        if (item.indexId == itemButton.indexId)
                            index = item.indexId
                    }
                }
            )
        },
        floatingActionButton = { FABCustom() },
        floatingActionButtonPosition = FabPosition.End,
        contentColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
    ) {
        Body(it, index)
    }
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
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    CardCustom(true)
                    CardCustom(true)
                    CardCustom(false)
                    CardCustom(true)
                    CardCustom(false)
                    CardCustom(false)
                    CardCustom(true)
                    CardCustom(false)
                    CardCustom(false)
                    CardCustom(false)
                    CardCustom(false)
                    CardCustom(true)
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp)){}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardCustom(
    isChecked: Boolean
) {
    val description =
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
    var isOpen by remember { mutableStateOf(false) }
    val height by isOpen.upDownIntegerAnimated(200, 65)

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(height.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = ELEVATION_DP),
        border = BorderStroke(0.5.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.clickable {
                isOpen = !isOpen
            },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            CheckCustom(
                checked = isChecked,
                onCheckedChange = {},
                enabled = true,
                animated = false,
                text = "Prueba de tareas",
            ) {}
            Column(modifier = Modifier.verticalScroll(rememberScrollState(), enabled = isOpen)) {
                Text(
                    text = description,
                    overflow = if (isOpen) TextOverflow.Visible else TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )
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