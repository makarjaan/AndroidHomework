package ru.itis.androidhomework.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.androidhomework.data.local.entities.FeaturesEntity
import ru.itis.androidhomework.data.local.entities.LocalEntity

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocal(local: LocalEntity)

    @Query("SELECT * FROM local WHERE LOWER(local_name) = LOWER(:localName) LIMIT 1")
    suspend fun getSavedLocal(localName: String): LocalEntity?

    @Query("SELECT * FROM local WHERE timestamp > :timestamp AND local_name != LOWER(:city)")
    suspend fun getOtherRequestsSince(timestamp: Long, city: String): List<LocalEntity>

    @Query("DELETE FROM local WHERE timestamp > :lastTimestamp AND local_name != LOWER(:city)")
    suspend fun clearIntermediateRequests(lastTimestamp: Long, city: String)

    @Update
    suspend fun updateLocal(local: LocalEntity)

    @Query("DELETE FROM local")
    suspend fun clearAll()

    @Query("SELECT * FROM local WHERE lat = :lat AND lon = :lon LIMIT 1")
    suspend fun getLocalByCoordinates(lat: Float, lon: Float): LocalEntity

}