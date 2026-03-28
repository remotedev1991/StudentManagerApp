package com.laddu.studentmanagerapp.moviedemo

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.laddu.studentmanagerapp.R
import kotlin.concurrent.thread

class ActorsActivity : AppCompatActivity() {

    private lateinit var database: MovieDatabase
    private lateinit var actorAdapter: ActorAdapter
    private val actorsList = mutableListOf<ActorWithMovie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actors)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = MovieDatabase.getInstance(this)
        setupUI()
        loadActors()
    }

    private fun setupUI() {
        val actorsListView = findViewById<ListView>(R.id.actors_list)

        // Setup adapter
        actorAdapter = ActorAdapter(
            this,
            actorsList,
            onDeleteClick = { actor ->
                deleteActor(actor)
            }
        )
        actorsListView.adapter = actorAdapter
    }

    private fun loadActors() {
        thread {
            val allActors = database.movieDao().getAllActorsWithMovies()
            runOnUiThread {
                actorsList.clear()
                actorsList.addAll(allActors)
                actorAdapter.notifyDataSetChanged()
                updateEmptyState()
            }
        }
    }

    private fun updateEmptyState() {
        val emptyState = findViewById<LinearLayout>(R.id.empty_state)
        val actorsListView = findViewById<ListView>(R.id.actors_list)

        if (actorsListView.count == 0) {
            emptyState.visibility = View.VISIBLE
        } else {
            emptyState.visibility = View.GONE
        }
    }

    private fun deleteActor(actorWithMovie: ActorWithMovie) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Delete Actor")
            .setMessage("Are you sure you want to delete \"${actorWithMovie.actor.name}\"?")
            .setPositiveButton("Yes") { _, _ ->
                thread {
                    database.movieDao().deleteActor(actorWithMovie.actor)
                    loadActors()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}

