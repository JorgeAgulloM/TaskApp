package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.data.DataOrException
import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import com.softyorch.taskapp.data.repository.PexelsRepository

class GetImage(private val repository: PexelsRepository) {
    suspend operator fun invoke(): DataOrException<Media, Boolean, Exception> {
        val dataOrException = DataOrException<Media, Boolean, Exception>()

        dataOrException.loading = true
        try {
            repository.getRandomImage().data.let { media ->
                dataOrException.data = media
            }
            dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.loading = false
            dataOrException.e = e
        }

        return dataOrException
    }
}