package ru.itis.androidhomework.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.itis.androidhomework.data.local.entities.LocalEntity

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocalName(local: LocalEntity)
}