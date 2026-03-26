package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieActorCrossRef(
    val movieId: Long,
    val actorId: Long
)
