package ru.itis.androidhomework.data.repository

import ru.itis.androidhomework.data.local.dao.LocalDao
import ru.itis.androidhomework.data.local.entities.LocalEntity
import ru.itis.androidhomework.data.local.mapper.LocalMapper
import ru.itis.androidhomework.data.remote.OpenTripMapApi
import ru.itis.androidhomework.data.remote.mapper.ApiResponseMapper
import ru.itis.androidhomework.domain.model.CoordinatesModel
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.model.LocalModel
import ru.itis.androidhomework.domain.repository.SearchFeaturesRepository
import javax.inject.Inject

class SearchFeaturesRepositoryImpl @Inject constructor(
    private val openTripMapApi: OpenTripMapApi,
    private val localDao: LocalDao,
    private val mapper: ApiResponseMapper,
    private val mapperLocal: LocalMapper
) : SearchFeaturesRepository {

    override suspend fun getCoordinates(city: String): CoordinatesModel {
        return openTripMapApi.getCityCoordinates(city = city).let(mapper::mapToCoordinates)
    }

    override suspend fun getFeatures(lat: Float, lon: Float): List<FeaturesModel> {
        val modelsList = openTripMapApi.getFeatures(lat = lat, lon = lon).let(mapper::mapToFeatures)
        return modelsList?.filter { it.xid != "" } ?: emptyList()
    }

    override suspend fun saveLocalName(local: LocalModel) {
        return localDao.saveLocalName(local = mapperLocal.mapToLocal(local))
    }
}