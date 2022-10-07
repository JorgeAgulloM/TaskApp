package com.softyorch.taskapp.data.network.pexels.response

import com.google.gson.annotations.SerializedName

data class VideoPicture(
    @SerializedName("id") val id: Int,
    @SerializedName("nr") val nr: Int,
    @SerializedName("picture") val picture: String
)