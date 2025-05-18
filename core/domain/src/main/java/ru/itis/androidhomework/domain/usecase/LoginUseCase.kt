package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.model.UserModel
import ru.itis.androidhomework.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(email: String, password: String): UserModel? {
        return withContext(ioDispatcher) {
            userRepository.getUserByEmailAndPassword(email, password)
        }
    }
}