/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.model

import com.softyorch.taskapp.ui.models.TaskModelUiMain

data class NewTaskModel(
    var title: String,
    var description: String
)

fun TaskModelUiMain.mapToNewTaskModel(): NewTaskModel =
    NewTaskModel(
        title = this.title,
        description = this.description
    )