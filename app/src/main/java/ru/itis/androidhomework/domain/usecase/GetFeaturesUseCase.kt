package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.exeption.WrongCoordinatesException
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.model.LocalModel
import ru.itis.androidhomework.domain.repository.SearchFeaturesRepository
import ru.itis.androidhomework.utils.Constants
import java.util.UUID
import javax.inject.Inject

class GetFeaturesUseCase @Inject constructor(
    private val searchRepository: SearchFeaturesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(city: String): List<FeaturesModel> {
        return withContext(ioDispatcher) {
            val coordinatesData = searchRepository.getCoordinates(city = city)
            if (coordinatesData.lat == Constants.EMPTY_FLOAT_DATA ||
                coordinatesData.lon == Constants.EMPTY_FLOAT_DATA) {
                throw WrongCoordinatesException(cause = null)
            } else {
                searchRepository.saveLocalName(
                    LocalModel(id = UUID.randomUUID().toString(), name = city)
                )

                searchRepository.getFeatures(
                    lat = coordinatesData.lat,
                    lon = coordinatesData.lon
                )
            }
        }
    }
}