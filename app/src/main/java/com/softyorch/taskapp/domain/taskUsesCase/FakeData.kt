/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository

class FakeData(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.fakeData()
}