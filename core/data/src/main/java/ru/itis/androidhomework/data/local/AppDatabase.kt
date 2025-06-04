package ru.itis.androidhomework.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.androidhomework.data.local.dao.FeatureDao
import ru.itis.androidhomework.data.local.dao.LocalDao
import ru.itis.androidhomework.data.local.dao.PushEventDao
import ru.itis.androidhomework.data.local.dao.RequestHistoryDao
import ru.itis.androidhomework.data.local.dao.UserDao
import ru.itis.androidhomework.data.local.entities.FeaturesEntity
import ru.itis.androidhomework.data.local.entities.LocalEntity
import ru.itis.androidhomework.data.local.entities.PushEventEntity
import ru.itis.androidhomework.data.local.entities.RequestHistoryEntity
import ru.itis.androidhomework.data.local.entities.UserEntity

@Database(
    entities = [
        LocalEntity::class,
        FeaturesEntity::class,
        RequestHistoryEntity::class,
        UserEntity::class,
        PushEventEntity::class],
    version = 3,
)
abstract class AppDatabase: RoomDatabase() {
    abstract val localDao: LocalDao
    abstract val featuresDao: FeatureDao
    abstract val requestHistoryDao: RequestHistoryDao
    abstract val pushEventDao: PushEventDao
    abstract val userDao: UserDao

    companion object {
        const val DB_LOG_KEY = "AppDB"
    }
}