package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataUseCase @Inject constructor(private val repository: DatastoreRepository) {
    operator fun invoke(): Resource<Flow<UserDataEntity>> = repository.getData()
}