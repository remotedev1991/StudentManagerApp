package com.laddu.studentmanagerapp.moviedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.laddu.studentmanagerapp.R

class MovieAdapter(
    context: Context,
    private val movies: MutableList<MovieWithActors>,
    private val onDeleteClick: (MovieWithActors) -> Unit,
    private val onAddActorClick: (MovieWithActors) -> Unit,
    private val onRemoveActorClick: (MovieWithActors, Actor) -> Unit
) : ArrayAdapter<MovieWithActors>(context, 0, movies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        }

        val movie = getItem(position)
        if (movie != null) {
            // Set movie basic info
            val titleText = view.findViewById<TextView>(R.id.movie_title_text)
            val genreText = view.findViewById<TextView>(R.id.movie_genre_badge)
            val yearText = view.findViewById<TextView>(R.id.movie_year_text)
            val directorText = view.findViewById<TextView>(R.id.movie_director_text)
            val budgetText = view.findViewById<TextView>(R.id.movie_budget_text)
            val descriptionText = view.findViewById<TextView>(R.id.movie_description_text)

            titleText.text = movie.movie.title
            genreText.text = movie.movie.genre
            yearText.text = movie.movie.releaseYear.toString()
            directorText.text = movie.movie.director
            budgetText.text = "$${movie.movie.budget}M"
            descriptionText.text = movie.movie.description

            // Delete button
            val deleteBtn = view.findViewById<ImageButton>(R.id.movie_delete_btn)
            deleteBtn.setOnClickListener {
                onDeleteClick(movie)
            }

            // Actors container
            val actorsContainer = view.findViewById<LinearLayout>(R.id.actors_container)
            val noActorsText = view.findViewById<TextView>(R.id.no_actors_text)
            val addActorBtn = view.findViewById<Button>(R.id.add_actor_btn)

            actorsContainer.removeAllViews()

            if (movie.actors.isEmpty()) {
                noActorsText.visibility = View.VISIBLE
            } else {
                noActorsText.visibility = View.GONE
                for (actor in movie.actors) {
                    val actorView = LayoutInflater.from(context).inflate(R.layout.actor_item, null)

                    val avatarText = actorView.findViewById<TextView>(R.id.actor_avatar)
                    val nameText = actorView.findViewById<TextView>(R.id.actor_name_text)
                    val birthText = actorView.findViewById<TextView>(R.id.actor_birth_text)
                    val nationText = actorView.findViewById<TextView>(R.id.actor_nation_text)
                    val removeBtn = actorView.findViewById<ImageButton>(R.id.remove_actor_btn)

                    // Set avatar to first letter of name
                    avatarText.text = actor.name.firstOrNull()?.toString() ?: "?"
                    nameText.text = actor.name
                    birthText.text = actor.birthYear.toString()
                    nationText.text = actor.nationality

                    removeBtn.setOnClickListener {
                        onRemoveActorClick(movie, actor)
                    }

                    actorsContainer.addView(actorView)
                }
            }

            addActorBtn.setOnClickListener {
                onAddActorClick(movie)
            }
        }

        return view
    }
}

