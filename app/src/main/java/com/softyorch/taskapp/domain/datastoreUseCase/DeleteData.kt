package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.repository.DatastoreRepository

class DeleteData (private val repository: DatastoreRepository) {
    suspend operator fun invoke() = repository.deleteData()
}