package com.laddu.studentmanagerapp.moviedemo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.laddu.studentmanagerapp.R
import kotlin.concurrent.thread

class MovieActivity : AppCompatActivity() {

    private lateinit var database: MovieDatabase
    private lateinit var movieAdapter: MovieAdapter
    private val moviesList = mutableListOf<MovieWithActors>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = MovieDatabase.getInstance(this)
        setupUI()
        loadMovies()
    }

    private fun setupUI() {
        val addMovieBtn = findViewById<Button>(R.id.add_movie_main)
        val addActorBtn = findViewById<Button>(R.id.add_actor_main)
        val viewActorsBtn = findViewById<Button>(R.id.view_actors_btn)
        val moviesListView = findViewById<ListView>(R.id.movies_list)

        addMovieBtn.setOnClickListener {
            showAddMovieDialog()
        }

        addActorBtn.setOnClickListener {
            showAddActorDialog()
        }

        viewActorsBtn.setOnClickListener {
            val intent = android.content.Intent(this, ActorsActivity::class.java)
            startActivity(intent)
        }

        // Setup adapter
        movieAdapter = MovieAdapter(
            this,
            moviesList,
            onDeleteClick = { movie ->
                deleteMovie(movie)
            },
            onAddActorClick = { movie ->
                showAssignActorDialog(movie)
            },
            onRemoveActorClick = { movie, actor ->
                removeActorFromMovie(movie, actor)
            }
        )
        moviesListView.adapter = movieAdapter
    }

    private fun loadMovies() {
        thread {
            val allMovies = database.movieDao().getAllMoviesWithActors()
            runOnUiThread {
                moviesList.clear()
                moviesList.addAll(allMovies)
                movieAdapter.notifyDataSetChanged()
                updateEmptyState()
            }
        }
    }

    private fun updateEmptyState() {
        val emptyState = findViewById<LinearLayout>(R.id.empty_state)
        val moviesListView = findViewById<ListView>(R.id.movies_list)

        if (moviesListView.count == 0) {
            emptyState.visibility = View.VISIBLE
        } else {
            emptyState.visibility = View.GONE
        }
    }

    private fun showAddMovieDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_movie, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val titleInput = dialogView.findViewById<EditText>(R.id.movie_title)
        val genreInput = dialogView.findViewById<EditText>(R.id.movie_genre)
        val yearInput = dialogView.findViewById<EditText>(R.id.movie_release_year)
        val directorInput = dialogView.findViewById<EditText>(R.id.movie_director)
        val budgetInput = dialogView.findViewById<EditText>(R.id.movie_budget)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.movie_description)

        val saveBtn = dialogView.findViewById<Button>(R.id.dialog_save_movie)
        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_cancel_movie)

        saveBtn.setOnClickListener {
            if (titleInput.text.isNotEmpty() && genreInput.text.isNotEmpty() &&
                yearInput.text.isNotEmpty() && directorInput.text.isNotEmpty() &&
                budgetInput.text.isNotEmpty() && descriptionInput.text.isNotEmpty()
            ) {
                val movie = Movie(
                    title = titleInput.text.toString(),
                    genre = genreInput.text.toString(),
                    releaseYear = yearInput.text.toString().toInt(),
                    director = directorInput.text.toString(),
                    budget = budgetInput.text.toString().toDouble(),
                    description = descriptionInput.text.toString()
                )

                thread {
                    database.movieDao().insertMovie(movie)
                    loadMovies()
                }

                dialog.dismiss()
            } else {
                android.widget.Toast.makeText(
                    this,
                    "Please fill all fields",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAddActorDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_actor, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val nameInput = dialogView.findViewById<EditText>(R.id.actor_name)
        val birthYearInput = dialogView.findViewById<EditText>(R.id.actor_birth_year)
        val nationalityInput = dialogView.findViewById<EditText>(R.id.actor_nationality)
        val professionInput = dialogView.findViewById<EditText>(R.id.actor_profession)
        val bioInput = dialogView.findViewById<EditText>(R.id.actor_bio)

        val saveBtn = dialogView.findViewById<Button>(R.id.dialog_save_actor)
        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_cancel_actor)

        saveBtn.setOnClickListener {
            if (nameInput.text.isNotEmpty() && birthYearInput.text.isNotEmpty() &&
                nationalityInput.text.isNotEmpty() && professionInput.text.isNotEmpty() &&
                bioInput.text.isNotEmpty()
            ) {
                val actor = Actor(
                    name = nameInput.text.toString(),
                    birthYear = birthYearInput.text.toString().toInt(),
                    nationality = nationalityInput.text.toString(),
                    profession = professionInput.text.toString(),
                    bio = bioInput.text.toString()
                )

                thread {
                    database.movieDao().insertActor(actor)
                }

                dialog.dismiss()
                android.widget.Toast.makeText(
                    this,
                    "Actor added successfully",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            } else {
                android.widget.Toast.makeText(
                    this,
                    "Please fill all fields",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAssignActorDialog(movie: MovieWithActors) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_assign_actors, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val listView = dialogView.findViewById<ListView>(R.id.actors_list_view)
        val saveBtn = dialogView.findViewById<Button>(R.id.dialog_save_assign)
        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_cancel_assign)

        thread {
            val allActors = database.movieDao().getAllActors()
            val currentActorIds = movie.actors.map { it.actorId }.toSet()

            runOnUiThread {
                val checkedStates = BooleanArray(allActors.size) { index ->
                    allActors[index].actorId in currentActorIds
                }

                listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, allActors.map { it.name })
                listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

                for (i in 0 until listView.count) {
                    listView.setItemChecked(i, checkedStates[i])
                }

                saveBtn.setOnClickListener {
                    val selectedActorIds = mutableListOf<Int>()
                    for (i in 0 until listView.count) {
                        if (listView.isItemChecked(i)) {
                            selectedActorIds.add(allActors[i].actorId)
                        }
                    }

                    thread {
                        for (actorId in selectedActorIds) {
                            if (actorId !in currentActorIds) {
                                database.movieDao().insertMovieActorCrossRef(
                                    MovieActorCrossRef(movie.movie.movieId.toLong(), actorId.toLong())
                                )
                            }
                        }
                        loadMovies()
                    }

                    dialog.dismiss()
                }
            }
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteMovie(movie: MovieWithActors) {
        AlertDialog.Builder(this)
            .setTitle("Delete Movie")
            .setMessage("Are you sure you want to delete \"${movie.movie.title}\"?")
            .setPositiveButton("Yes") { _, _ ->
                thread {
                    database.movieDao().deleteMovie(movie.movie)
                    loadMovies()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun removeActorFromMovie(movie: MovieWithActors, actor: Actor) {
        android.widget.Toast.makeText(
            this,
            "Remove actor - use the edit feature to update cast",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}