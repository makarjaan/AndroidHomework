package ru.itis.androidhomework.domain.repository

import ru.itis.androidhomework.domain.model.CoordinatesModel
import ru.itis.androidhomework.domain.model.FeaturesModel

interface SearchFeaturesRepository {

    suspend fun getCoordinates(city: String) : CoordinatesModel

    suspend fun getFeatures(lat: Float, lon: Float) : List<FeaturesModel>

}