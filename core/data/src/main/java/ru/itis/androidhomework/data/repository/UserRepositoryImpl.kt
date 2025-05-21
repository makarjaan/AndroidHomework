package ru.itis.androidhomework.data.repository

import ru.itis.androidhomework.data.local.dao.UserDao
import ru.itis.androidhomework.data.local.mapper.DbMapper
import ru.itis.androidhomework.domain.model.UserModel
import ru.itis.androidhomework.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dbMapper: DbMapper
) : UserRepository {

    override suspend fun getUserByEmailAndPassword(email: String, password: String): UserModel? {
        val userEntity = userDao.getUserByEmailAndPassword(email, password)
        return userEntity?.let { dbMapper.mapToUserModel(it) }
    }

    override suspend fun saveUser(user: UserModel) {
        val userEntity = dbMapper.mapToUserEntity(user)
        userDao.saveUser(userEntity)
    }
}
