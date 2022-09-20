package com.softyorch.taskapp.domain.datastoreUseCase

data class DatastoreUseCases(
    val getData: GetData,
    val saveData: SaveData,
    val deleteData: DeleteData
)
