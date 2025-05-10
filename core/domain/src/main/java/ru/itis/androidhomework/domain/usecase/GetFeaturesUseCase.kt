package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.exeption.WrongCoordinatesException
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.model.FeaturesResult
import ru.itis.androidhomework.domain.repository.SearchFeaturesRepository
import javax.inject.Inject


class GetFeaturesUseCase @Inject constructor(
    private val searchRepository: SearchFeaturesRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(city: String): FeaturesResult {
        return withContext(ioDispatcher) {
            val feature = searchRepository.getFeatures(city)

            if (feature.features.size == 1 && feature.features.first() == FeaturesModel.EMPTY) {
                throw WrongCoordinatesException(cause = null)
            }

            feature
        }
    }
}