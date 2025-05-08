package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.data.repository.FeatureDataRepositoryImpl
import ru.itis.androidhomework.domain.model.FeatureDetailsModel
import javax.inject.Inject

class GetFeatureDetailsUseCase @Inject constructor(
    private val detailsRepository: FeatureDataRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: String): FeatureDetailsModel {
        return withContext(ioDispatcher) {
            detailsRepository.getFeatureInfo(id)
        }
    }

}