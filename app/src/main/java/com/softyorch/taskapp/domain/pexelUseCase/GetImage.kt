package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.data.network.pexels.response.Photo
import com.softyorch.taskapp.data.repository.PexelsRepository

class GetImage(private val repository: PexelsRepository) {

    suspend operator fun invoke(queryParam: String): Photo =
        repository.getRandomImage(queryParam)
}