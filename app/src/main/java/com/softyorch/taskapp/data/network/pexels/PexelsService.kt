package com.softyorch.taskapp.data.network.pexels

import com.softyorch.taskapp.data.network.pexels.response.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PexelsService @Inject constructor(private val pexelsClient: PexelsClient) {
    suspend fun getMediaList(): List<Media?> {
        return withContext(Dispatchers.IO) {
            try {
                pexelsClient.getPhotoCollections().body()?.media ?: emptyList()
            } catch (ex: Exception) {
                emptyList()
            }
        }
    }
}