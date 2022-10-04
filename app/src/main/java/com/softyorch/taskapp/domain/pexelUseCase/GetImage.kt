package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import com.softyorch.taskapp.data.repository.PexelsRepository

class GetImage(private val repository: PexelsRepository) {

    suspend operator fun invoke(): Media =
        repository.getRandomImage()
}