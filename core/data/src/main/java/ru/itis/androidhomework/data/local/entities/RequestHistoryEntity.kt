package ru.itis.androidhomework.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests_history")
data class RequestHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
