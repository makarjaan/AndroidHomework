package ru.itis.androidhomework.domain.repository

import ru.itis.androidhomework.domain.model.FeatureDetailsModel

interface FeatureDataRepository {

    suspend fun getFeatureInfo(id: String) : FeatureDetailsModel

}