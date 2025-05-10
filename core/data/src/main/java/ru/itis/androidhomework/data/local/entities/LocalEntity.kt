package ru.itis.androidhomework.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local")
data class LocalEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "local_name")
    val localName: String,
    @ColumnInfo(name = "lon")
    val lon: Float,
    @ColumnInfo(name = "lat")
    val lat: Float,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)