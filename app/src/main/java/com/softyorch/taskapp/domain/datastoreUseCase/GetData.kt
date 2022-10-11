package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow

class GetData (private val repository: DatastoreRepository) {
    operator fun invoke(): Flow<UserDataEntity>? = repository.getData()
}