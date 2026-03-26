package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ActorWithMovie(
    @Embedded
    val actor: Actor,

    @Relation(
        parentColumn = "actorId",
        entityColumn = "movieId",
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val movies: List<Movie>
)


//list of movies of Leonardo DiCaprio