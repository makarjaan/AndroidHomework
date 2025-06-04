package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.repository.UserRepository
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Boolean {
        return withContext(ioDispatcher) {
            userRepository.isUserAuthorized()
        }
    }
}