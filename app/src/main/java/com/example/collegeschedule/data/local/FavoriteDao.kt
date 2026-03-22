package com.example.collegeschedule.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE groupName = :groupName")
    suspend fun removeFavorite(groupName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE groupName = :groupName)")
    suspend fun isFavorite(groupName: String): Boolean
}