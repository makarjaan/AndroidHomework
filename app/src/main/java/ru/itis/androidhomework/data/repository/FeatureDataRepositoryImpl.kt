package ru.itis.androidhomework.data.repository

import ru.itis.androidhomework.data.remote.OpenTripMapApi
import ru.itis.androidhomework.data.remote.mapper.ApiResponseMapper
import ru.itis.androidhomework.domain.model.FeatureDetailsModel
import ru.itis.androidhomework.domain.repository.FeatureDataRepository
import javax.inject.Inject

class FeatureDataRepositoryImpl @Inject constructor(
    private val openTripMapApi: OpenTripMapApi,
    private val mapper: ApiResponseMapper
): FeatureDataRepository {

    override suspend fun getFeatureInfo(id: String): FeatureDetailsModel {
        return openTripMapApi.getFeatureDetails(id).let(mapper::mapToDetails)
    }
}