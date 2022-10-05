package com.softyorch.taskapp.utils

import com.softyorch.taskapp.data.network.pexels.response.*
import com.softyorch.taskapp.data.repository.pexels.model.MediaModel
import com.softyorch.taskapp.domain.pexelUseCase.MediaModelDomain

class DataOrError<T, E> {
    var data: T? = null
    var error: E? = null

    companion object{
        val listResponse = DataOrError<List<Media>, String>()
        val mediaModelResponse = DataOrError<MediaModel, String>()
        val mediaModelDomain = DataOrError<MediaModelDomain, String>()
    }
}