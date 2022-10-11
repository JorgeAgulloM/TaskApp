package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.data.repository.pexels.PexelsRepository

class GetImage(private val repository: PexelsRepository) {
    suspend operator fun invoke(): MediaModelDomain =
        repository.getRandomImage().mapToMediaModelDomain()
}