package ru.itis.androidhomework.data.repository

import ru.itis.androidhomework.data.local.dao.FeatureDao
import ru.itis.androidhomework.data.local.dao.LocalDao
import ru.itis.androidhomework.data.local.dao.RequestHistoryDao
import ru.itis.androidhomework.data.local.entities.FeaturesEntity
import ru.itis.androidhomework.data.local.entities.LocalEntity
import ru.itis.androidhomework.data.local.entities.RequestHistoryEntity
import ru.itis.androidhomework.data.remote.OpenTripMapApi
import ru.itis.androidhomework.data.remote.mapper.ApiResponseMapper
import ru.itis.androidhomework.data.local.mapper.DbMapper
import ru.itis.androidhomework.domain.model.CoordinatesModel
import ru.itis.androidhomework.domain.model.DataSource
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.model.FeaturesResult
import ru.itis.androidhomework.domain.repository.SearchFeaturesRepository
import ru.itis.androidhomework.domain.utils.Constants
import java.util.UUID
import javax.inject.Inject

class SearchFeaturesRepositoryImpl @Inject constructor(
    private val openTripMapApi: OpenTripMapApi,
    private val localDao: LocalDao,
    private val featureDao: FeatureDao,
    private val requestHistoryDao: RequestHistoryDao,
    private val mapper: ApiResponseMapper,
    private val dbMapper: DbMapper
) : SearchFeaturesRepository {

    override suspend fun getFeatures(city: String): FeaturesResult {

        val savedRequest = localDao.getSavedLocal(city)
        val currentTime = System.currentTimeMillis()
        val lowLocalName = city.lowercase()

        requestHistoryDao.insertRequest(
            RequestHistoryEntity(
                city = city.lowercase(),
                timestamp = currentTime
            )
        )


        if (savedRequest != null) {

            val isCacheValid = currentTime - savedRequest.timestamp < CACHE_TIME_LIMIT

            val requestCountSinceLastUpdate = requestHistoryDao.getRequestCountSince(city.lowercase(), savedRequest.timestamp)
            val ignoreCache = requestCountSinceLastUpdate >= MAX_REQUESTS_BEFORE_IGNORING_CACHE

            //достаём из кэша
            if (isCacheValid && !ignoreCache) {
                val cachedFeatures = getFeaturesFromCache(savedRequest.lat, savedRequest.lon)
                return FeaturesResult(cachedFeatures, DataSource.CACHE)
            }

            // обновляем
            val apiResponse = openTripMapApi.getCityCoordinates(city).let(mapper::mapToCoordinates)
            localDao.updateLocal(
                LocalEntity(
                    id = savedRequest.id,
                    localName = savedRequest.localName,
                    lat = apiResponse.lat,
                    lon = apiResponse.lon,
                    timestamp = currentTime,
                )
            )

            val modelsList = openTripMapApi.getFeatures(apiResponse.lat, apiResponse.lon).let(mapper::mapToFeatures)
            val filtered = modelsList?.filter { it.xid.isNotBlank() } ?: emptyList()
            updateFeatures(filtered, savedRequest.id)

            return FeaturesResult(filtered, DataSource.API)
        }

        //сохраняем
        val apiResponse = openTripMapApi.getCityCoordinates(lowLocalName).let(mapper::mapToCoordinates)
        if (apiResponse != CoordinatesModel.EMPTY) {
            saveLocal(city, apiResponse.lat, apiResponse.lon)
            val modelsList = openTripMapApi.getFeatures(lat = apiResponse.lat, lon = apiResponse.lon).let(mapper::mapToFeatures)
            val filtered = modelsList?.filter { it.xid != Constants.EMPTY_STRING } ?: emptyList()

            localDao.getSavedLocal(city)?.let { saveFeature(filtered, it.id) }
            return FeaturesResult(filtered, DataSource.API)

        } else {
            return FeaturesResult(listOf(FeaturesModel.EMPTY), DataSource.API)
        }
    }


    private suspend fun getFeaturesFromCache(lat: Float, lon: Float): List<FeaturesModel> {
        val local = localDao.getLocalByCoordinates(lat, lon)
        val features = featureDao.getFeaturesByLocalId(local.id)
        if (features.isNotEmpty()) {
            return features.map(dbMapper::mapToFeatureModel)
        }
        return emptyList()
    }


    private suspend fun updateFeatures(list: List<FeaturesModel>, localId: String) {
        val listEntity = list.map { model ->
            dbMapper.mapToFeatureModelToEntity(model, localId)
        }
        featureDao.updateFeatures(listEntity)
    }


    private suspend fun saveLocal(local: String, lat: Float, lon: Float) {
        return localDao.saveLocal(
            LocalEntity(
                id = UUID.randomUUID().toString(),
                localName = local,
                lat = lat,
                lon = lon,
                timestamp = System.currentTimeMillis(),
            )
        )
    }

    private suspend fun saveFeature(features: List<FeaturesModel>?, localId: String) {

        if (features.isNullOrEmpty()) {
            return
        }

        val entities = features.map { feature ->
            FeaturesEntity(
                featureId = feature.xid,
                localId = localId,
                name = feature.name,
                rate = feature.rate,
                kinds = feature.kinds
            )
        }

        featureDao.saveFeatures(entities)
    }


    companion object {
        const val CACHE_TIME_LIMIT = 3 * 60 * 1000L
        const val MAX_REQUESTS_BEFORE_IGNORING_CACHE = 3
    }
}