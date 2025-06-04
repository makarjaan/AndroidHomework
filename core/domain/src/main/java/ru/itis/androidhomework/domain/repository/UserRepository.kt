package ru.itis.androidhomework.domain.repository

import ru.itis.androidhomework.domain.model.UserModel

interface UserRepository {

    suspend fun getUserByEmailAndPassword(email: String, password: String): UserModel?

    suspend fun saveUser(user: UserModel)

    suspend fun isUserAuthorized(): Boolean

}