package ru.itis.androidhomework.data.remote.pojo.response

import com.google.gson.annotations.SerializedName

class CoordinatesResponse (
    @SerializedName("lat")
    val lat: Float?,
    @SerializedName("lon")
    val lon: Float?
)