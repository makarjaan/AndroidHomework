package ru.itis.androidhomework.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.itis.androidhomework.data.remote.pojo.response.CoordinatesResponse
import ru.itis.androidhomework.data.remote.pojo.response.FeatureDetailsResponse
import ru.itis.androidhomework.data.remote.pojo.response.FeaturesResponse

interface OpenTripMapApi {

    @GET("places/geoname?country=RU")
    suspend fun getCityCoordinates(
        @Query("name") city: String
    ): CoordinatesResponse?

    @GET("places/radius?radius=30000&src_attr=wikidata")
    suspend fun getFeatures(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
    ): FeaturesResponse?

    @GET("places/xid/{xid}")
    suspend fun getFeatureDetails(
        @Path("xid") id: String
    ): FeatureDetailsResponse?
}