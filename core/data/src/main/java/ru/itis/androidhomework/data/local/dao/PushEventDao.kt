package ru.itis.androidhomework.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.androidhomework.data.local.entities.PushEventEntity

@Dao
interface PushEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: PushEventEntity)

    @Query("SELECT * FROM push_events ORDER BY timestamp DESC")
    suspend fun getAll(): List<PushEventEntity>
}
