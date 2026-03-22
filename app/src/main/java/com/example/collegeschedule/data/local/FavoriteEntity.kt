package com.example.collegeschedule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val groupName: String,
    val course: Int,
    val specialtyName: String?,
    val addedAt: Long = System.currentTimeMillis()
)