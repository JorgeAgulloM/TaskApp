package com.softyorch.taskapp.domain.datastoreUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.DatastoreRepository
import javax.inject.Inject

class SaveDataUseCase @Inject constructor(private val repository: DatastoreRepository) {
    suspend operator fun invoke(userDataEntity: UserDataEntity) =
        repository.saveData(userDataEntity = userDataEntity)
}