package ru.itis.androidhomework.domain.repository

interface PushRepository {
    suspend fun saveEvent(event: String, city: String)
}
