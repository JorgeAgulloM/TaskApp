package com.softyorch.taskapp.data.network.pexels.response

import com.google.gson.annotations.SerializedName

data class PexelsMyCollections(
    @SerializedName("id") val id: String,
    @SerializedName("media") val media: List<Media>,
    @SerializedName("next_page") val nextPage: String,
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("prev_page") val prevPage: String,
    @SerializedName("total_results") val totalResults: Int
)