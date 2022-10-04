package com.softyorch.taskapp.data.network.pexels

import com.softyorch.taskapp.data.network.pexels.response.PexelsPhotos
import com.softyorch.taskapp.ui.activities.KEY_API_PEXELS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface PexelsClient {
    @Headers("Authorization: $KEY_API_PEXELS")
    @GET("v1/search")
    suspend fun getPhoto(
        @Query("query") queryParam: String,
        @Query("per_page") perPageParam: String = "1"
    ): Response<PexelsPhotos>
}