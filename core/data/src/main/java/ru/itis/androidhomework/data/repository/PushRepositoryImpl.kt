package ru.itis.androidhomework.data.repository

import ru.itis.androidhomework.domain.repository.PushRepository
import javax.inject.Inject
import ru.itis.androidhomework.data.local.dao.PushEventDao
import ru.itis.androidhomework.data.local.entities.PushEventEntity

class PushRepositoryImpl @Inject constructor(
    private val dao: PushEventDao
) : PushRepository {

    override suspend fun saveEvent(event: String, city: String) {
        val entity = PushEventEntity(
            event = event,
            city = city,
            timestamp = System.currentTimeMillis()
        )
        dao.insert(entity)
    }
}
