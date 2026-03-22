package com.example.collegeschedule.data.repository

import com.example.collegeschedule.data.local.FavoriteDao
import com.example.collegeschedule.data.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteDao) {
    val favorites: Flow<List<FavoriteEntity>> = dao.getAllFavorites()

    suspend fun addFavorite(groupName: String, course: Int, specialtyName: String?) {
        dao.addFavorite(FavoriteEntity(groupName, course, specialtyName))
    }

    suspend fun removeFavorite(groupName: String) {
        dao.removeFavorite(groupName)
    }

    suspend fun isFavorite(groupName: String): Boolean {
        return dao.isFavorite(groupName)
    }
}