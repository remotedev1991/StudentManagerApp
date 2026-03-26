package com.laddu.studentmanagerapp.moviedemo

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.laddu.studentmanagerapp.R

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = MovieDatabase.getInstance(this)

        var movieId: Long ? = 0
        var actorId: Long ? = 0

        val addMoview = findViewById<android.widget.Button>(R.id.add_movie)
        addMoview.setOnClickListener {
            Thread {
                movieId = database.movieDao().insertMovie(Movie(title = "Inception"))
            }.start()
        }

        val addActor = findViewById<android.widget.Button>(R.id.add_actor)
        addActor.setOnClickListener {
            Thread {
                actorId = database.movieDao().insertActor(Actor(name = "Leonardo DiCaprio"))
                if (movieId != null && actorId != null) {
                    database.movieDao().insertMovieActorCrossRef(MovieActorCrossRef(movieId!!, actorId!!))
                }
            }.start()
        }

        val showData = findViewById<android.widget.Button>(R.id.show_data)
        showData.setOnClickListener {
            Thread {
                val moviesWithActors = database.movieDao().getMoviesWithActors(movieId ?: 0)
                val actorsWithMovies = database.movieDao().getActorsWithMovies(actorId ?: 0)

                runOnUiThread {
                   val data = findViewById<TextView>(R.id.data)
                   data.text = "$moviesWithActors \n\n $actorsWithMovies"
                }
            }.start()
        }


    }
}