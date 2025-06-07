package ru.itis.androidhomework.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "push_events")
data class PushEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("event")
    val event: String,
    @ColumnInfo("city")
    val city: String,
    @ColumnInfo("timestamp")
    val timestamp: Long
)
