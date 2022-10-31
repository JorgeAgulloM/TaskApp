/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class NewTaskModel(
    var title: String = emptyString,
    var description: String = emptyString,
    var author: String = emptyString,
    var entryDate: Date = Date.from(Instant.now())
)