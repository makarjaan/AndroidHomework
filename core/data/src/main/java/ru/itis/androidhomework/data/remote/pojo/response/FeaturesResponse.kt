package ru.itis.androidhomework.data.remote.pojo.response

import com.google.gson.annotations.SerializedName

class FeaturesResponse (
    @SerializedName("features")
    val features: List<PlaceFeatureResponse>
)

class PlaceFeatureResponse(
    @SerializedName("properties")
    val properties: PlacePropertiesResponse
)

class PlacePropertiesResponse(
    @SerializedName("xid")
    val xid: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("rate")
    val rate: Int?,
    @SerializedName("kinds")
    val kinds: String?
)
