package ru.itis.androidhomework.domain.repository

import ru.itis.androidhomework.domain.model.CoordinatesModel
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.model.LocalModel

interface SearchFeaturesRepository {

    suspend fun getCoordinates(city: String) : CoordinatesModel

    suspend fun getFeatures(lat: Float, lon: Float) : List<FeaturesModel>

    suspend fun saveLocalName(local: LocalModel)

}