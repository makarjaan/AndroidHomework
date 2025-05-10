package ru.itis.androidhomework.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.itis.androidhomework.data.local.entities.RequestHistoryEntity

@Dao
interface RequestHistoryDao {

    @Insert
    suspend fun insertRequest(request: RequestHistoryEntity)

    @Query("SELECT COUNT(*) FROM requests_history WHERE city != :city AND timestamp > :since")
    suspend fun getRequestCountSince(city: String, since: Long): Int
}
