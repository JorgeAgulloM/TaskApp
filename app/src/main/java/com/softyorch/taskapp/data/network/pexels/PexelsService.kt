package com.softyorch.taskapp.data.network.pexels

import android.util.Log
import com.softyorch.taskapp.data.network.pexels.response.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class PexelsService @Inject constructor(private val retrofit: Retrofit) {

    suspend fun getImages(queryParam: String): List<Photo> =
        withContext(Dispatchers.IO) {
            retrofit.create(PexelsClient::class.java).getPhoto(queryParam).let { response ->
                if (response.body() != null){
                    Log.d("PHOTOS", "${response.body()!!.photos}")
                    response.body()!!.photos
                } else {
                    emptyList()
                }
            }
        }

}