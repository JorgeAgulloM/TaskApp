/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

sealed interface StateMain{
    object Main: StateMain
    object NewTask: StateMain
    object Settings: StateMain
}