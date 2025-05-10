package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.model.FeatureDetailsModel
import ru.itis.androidhomework.domain.repository.FeatureDataRepository
import javax.inject.Inject

class GetFeatureDetailsUseCase @Inject constructor(
    private val detailsRepository: FeatureDataRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: String): FeatureDetailsModel {
        return withContext(ioDispatcher) {
            detailsRepository.getFeatureInfo(id)
        }
    }
}