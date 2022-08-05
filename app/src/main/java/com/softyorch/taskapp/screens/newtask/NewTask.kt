package com.softyorch.taskapp.screens.newtask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.PrimaryButton
import com.softyorch.taskapp.utils.TextFieldTask
import com.softyorch.taskapp.utils.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTask(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = "New Task",
            icon = Icons.Rounded.Settings,
            nameScreen = TaskAppScreens.SettingsScreen.name,
            navController = navController,
        )
    },
        content = {
            Content(it = it)
        })
}

@Composable
fun Content(it: PaddingValues) {
    Column(
        modifier = Modifier.padding(top = it.calculateTopPadding(), start = 4.dp, end = 4.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RowIndication(text = "Created by: ", textEdit = "Jorge Agulló")
        RowIndication(text = "Created data: ", textEdit = "05/08/2022")
        RowIndication(text = "Name of task: ")
        TextFieldTask(
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            icon = Icons.Rounded.TextFields,
            contentDescription = "name",
            newTask = true,
        )
        RowIndication(text = "Task description: ")
        TextFieldTask(
            label = "descripción",
            placeholder = "Escribe algo...",
            icon = Icons.Rounded.TextFields,
            contentDescription = "description",
            multiLine = true,
            newTask = true,
        )
        Row(
            modifier = Modifier.size(width = 300.dp, height = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrimaryButton()
            PrimaryButton()
        }
    }
}

@Composable
private fun RowIndication(
    text: String,
    textEdit: String = ""
) {

    val arrangement = if (textEdit == "") Arrangement.Start else Arrangement.SpaceBetween

    Row(
        modifier = Modifier.size(width = 300.dp, height = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = arrangement
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = textEdit,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}