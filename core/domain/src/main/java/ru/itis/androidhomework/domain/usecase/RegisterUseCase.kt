package ru.itis.androidhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.androidhomework.domain.model.UserModel
import ru.itis.androidhomework.domain.repository.UserRepository
import java.util.UUID
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(userName: String, email: String, password: String) {
        return withContext(ioDispatcher) {
            val user = UserModel(
                id = UUID.randomUUID().toString(),
                userName = userName,
                email = email,
                password = password
            )
            userRepository.saveUser(user)
        }
    }
}
