package ru.itis.androidhomework.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.androidhomework.data.local.dao.FeatureDao
import ru.itis.androidhomework.data.local.dao.LocalDao
import ru.itis.androidhomework.data.local.entities.FeaturesEntity
import ru.itis.androidhomework.data.local.entities.LocalEntity

@Database(
    entities = [LocalEntity::class, FeaturesEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract val localDao: LocalDao
    abstract val featuresDao: FeatureDao

    companion object {
        const val DB_LOG_KEY = "AppDB"
    }
}