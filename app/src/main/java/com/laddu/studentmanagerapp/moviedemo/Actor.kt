package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val actorId: Int = 0,
    val name: String,
)
