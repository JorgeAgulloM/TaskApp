package com.softyorch.taskapp.data.network.pexels

import com.softyorch.taskapp.data.DataOrException
import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class PexelsService @Inject constructor(private val retrofit: Retrofit) {

    suspend fun getMediaList(): DataOrException<List<Media>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Media>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = withContext(Dispatchers.IO) {
                retrofit.create(PexelsClient::class.java).getPhotoCollections().let { response ->
                    dataOrException.loading = false
                    if (response.body() != null) {
                        response.body()!!.media
                    } else {
                        emptyList()
                    }
                }
            }
        } catch (e: Exception) {
            dataOrException.loading = false
            dataOrException.e = e
        }

        return dataOrException
    }

}