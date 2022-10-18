/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

sealed interface TaskLoad{
    object UncheckedTask:TaskLoad
    object CheckedTask:TaskLoad
    object AllTask:TaskLoad
}