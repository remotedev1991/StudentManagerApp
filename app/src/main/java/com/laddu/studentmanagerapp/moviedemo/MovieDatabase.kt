package com.laddu.studentmanagerapp.moviedemo

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Movie::class, Actor::class, MovieActorCrossRef::class], version = 2)
abstract class MovieDatabase: RoomDatabase() {
  abstract fun movieDao(): MovieDao

  companion object {
      @Volatile
        private var INSTANCE: MovieDatabase? = null

      fun getInstance(context: Context): MovieDatabase {
          return INSTANCE ?: synchronized(this) {
              val instance = androidx.room.Room.databaseBuilder(
                  context,
                  MovieDatabase::class.java,
                  "movie_database"
              )
              .fallbackToDestructiveMigration() // For development - recreates database on schema change
              .build()
              INSTANCE = instance
              instance
          }
      }
  }

}