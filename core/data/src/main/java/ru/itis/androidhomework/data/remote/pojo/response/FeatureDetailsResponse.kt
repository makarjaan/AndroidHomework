package ru.itis.androidhomework.data.remote.pojo.response

import com.google.gson.annotations.SerializedName

class FeatureDetailsResponse (
    @SerializedName("name")
    val name: String?,
    @SerializedName("address")
    val address: Address?,
    @SerializedName("rate")
    val rate: String?,
    @SerializedName("wikipedia")
    val wikipedia: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("wikipedia_extracts")
    val text: WikipediaExtracts?
)

class Address(
    @SerializedName("country")
    val country: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("city_district")
    val district: String,
    @SerializedName("suburb")
    val suburb: String?,
    @SerializedName("pedestrian")
    val pedestrian: String?,
    @SerializedName("house_number")
    val houseNumber: String?,
    @SerializedName("road")
    val road: String?
)

class WikipediaExtracts(
    @SerializedName("text")
    val text: String?
)