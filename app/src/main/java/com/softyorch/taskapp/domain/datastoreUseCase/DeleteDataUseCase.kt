package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.repository.DatastoreRepository
import javax.inject.Inject

class DeleteDataUseCase @Inject constructor(private val repository: DatastoreRepository) {
    suspend operator fun invoke() = repository.deleteData()
}