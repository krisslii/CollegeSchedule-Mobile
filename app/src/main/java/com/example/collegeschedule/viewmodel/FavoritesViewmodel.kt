package com.example.collegeschedule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeschedule.data.local.FavoriteEntity
import com.example.collegeschedule.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FavoriteRepository) : ViewModel() {

    private val _favorites = MutableStateFlow<List<FavoriteEntity>>(emptyList())
    val favorites: StateFlow<List<FavoriteEntity>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.favorites.collect { favorites ->
                _favorites.value = favorites
            }
        }
    }

    fun addFavorite(groupName: String, course: Int, specialtyName: String?) {
        viewModelScope.launch {
            repository.addFavorite(groupName, course, specialtyName)
        }
    }

    fun removeFavorite(groupName: String) {
        viewModelScope.launch {
            repository.removeFavorite(groupName)
        }
    }

    suspend fun isFavorite(groupName: String): Boolean {
        return repository.isFavorite(groupName)
    }
}