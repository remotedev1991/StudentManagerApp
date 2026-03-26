package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MovieDao {

    @Insert
    fun insertMovie(movie: Movie): Long

    @Insert
    fun insertActor(actor: Actor): Long

    @Insert
    fun insertMovieActorCrossRef(crossRef: MovieActorCrossRef)

    @Transaction
    @Query("select * from movies where movieId=:movieId")
    fun getMoviesWithActors(movieId: Long): List<MovieWithActors>

    @Transaction
    @Query("SELECT * FROM actors WHERE actorId = :id")
    fun getActorsWithMovies(id: Long): List<ActorWithMovie>

}