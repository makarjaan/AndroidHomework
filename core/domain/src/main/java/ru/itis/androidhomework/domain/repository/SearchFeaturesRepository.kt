package ru.itis.androidhomework.domain.repository

import ru.itis.androidhomework.domain.model.FeaturesResult

interface SearchFeaturesRepository {

    suspend fun getFeatures(city: String) : FeaturesResult

}