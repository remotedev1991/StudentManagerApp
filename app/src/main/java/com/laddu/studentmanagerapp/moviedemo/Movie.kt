package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    val title: String,
    val genre: String,
    val releaseYear: Int,
    val director: String,
    val budget: Double,
    val description: String,
)
