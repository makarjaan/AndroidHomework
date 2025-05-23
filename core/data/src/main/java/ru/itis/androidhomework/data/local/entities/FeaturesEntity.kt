package ru.itis.androidhomework.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_features",
    foreignKeys = [
        ForeignKey(
            entity = LocalEntity::class,
            parentColumns = ["id"],
            childColumns = ["local_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["local_id"])]
)
data class FeaturesEntity(
    @PrimaryKey
    @ColumnInfo(name = "feature_id")
    val featureId: String,

    @ColumnInfo(name = "local_id")
    val localId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "rate")
    val rate: Int,

    @ColumnInfo(name = "kinds")
    val kinds: String
)

