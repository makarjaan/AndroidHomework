package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.repository.PushRepository
import javax.inject.Inject

class SavePushEventUseCase @Inject constructor(
    private val pushRepository: PushRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(event: String, city: String) {
        return withContext(ioDispatcher) {
            pushRepository.saveEvent(event, city)
        }
    }
}
