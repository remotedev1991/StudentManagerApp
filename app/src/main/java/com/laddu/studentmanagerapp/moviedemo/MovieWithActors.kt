package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithActors(
    @Embedded
    val movie: Movie,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "actorId",
        associateBy = Junction(MovieActorCrossRef::class)
    ) val actors: List<Actor>
)
