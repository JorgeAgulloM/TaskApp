package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.repository.DatastoreRepository

class GetData (private val repository: DatastoreRepository) {
    operator fun invoke() = repository.getData()
}