package com.laddu.studentmanagerapp.moviedemo

import androidx.room.Dao
import androidx.room.Delete
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
    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    fun getMoviesWithActors(movieId: Long): List<MovieWithActors>

    @Transaction
    @Query("SELECT * FROM actors WHERE actorId = :id")
    fun getActorsWithMovies(id: Long): List<ActorWithMovie>

    @Transaction
    @Query("SELECT * FROM movies")
    fun getAllMoviesWithActors(): List<MovieWithActors>

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    @Transaction
    @Query("SELECT * FROM actors")
    fun getAllActorsWithMovies(): List<ActorWithMovie>

    @Query("SELECT * FROM actors")
    fun getAllActors(): List<Actor>

    @Delete
    fun deleteMovie(movie: Movie)

    @Delete
    fun deleteActor(actor: Actor)

}