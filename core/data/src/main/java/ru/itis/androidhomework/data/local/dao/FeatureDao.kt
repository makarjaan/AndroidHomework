package ru.itis.androidhomework.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.itis.androidhomework.data.local.entities.FeaturesEntity

@Dao
interface FeatureDao {

    @Query("SELECT * FROM search_features WHERE :localId = local_id")
    suspend fun getFeaturesByLocalId(localId: String): MutableList<FeaturesEntity>

    @Update
    suspend fun updateFeatures(feature: List<FeaturesEntity>)

    @Insert
    suspend fun saveFeatures(features: List<FeaturesEntity>)
}