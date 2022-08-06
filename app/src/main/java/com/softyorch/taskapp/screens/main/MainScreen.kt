package com.softyorch.taskapp.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.FAB
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.RowIndication
import com.softyorch.taskapp.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Main",
                icon = Icons.Rounded.Home,
                isMainScreen = true,
                nameScreen = TaskAppScreens.MainScreen.name,
                navController = navController,
            )
        },
        floatingActionButton = {
            FAB(
                navController = navController
            )
        },
    ) {
        Content(it)
    }
}

@Composable
fun Content(it: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 32.dp, end = 8.dp)
    ) {
        RowIndication(text = "My Tasks")
        RowIndication(text = "To be made...", fontSize = 16.sp)
        //Aquí debe ir un LazyColumn
        Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {

            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
        }
        RowIndication(text = "To be made...", fontSize = 16.sp)
        //Aquí debe ir un LazyColumn
        Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {

            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
            Text(text = "Tareas listadas")
        }
    }
}