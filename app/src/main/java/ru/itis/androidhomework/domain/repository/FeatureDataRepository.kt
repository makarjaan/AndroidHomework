package ru.itis.androidhomework.domain.repository

interface FeatureDataRepository {

    suspend fun getFeatureInfo(id: String) : Any?

}