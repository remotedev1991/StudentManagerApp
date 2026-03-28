package com.laddu.studentmanagerapp.moviedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.laddu.studentmanagerapp.R

class ActorAdapter(
    context: Context,
    private val actors: MutableList<ActorWithMovie>,
    private val onDeleteClick: (ActorWithMovie) -> Unit
) : ArrayAdapter<ActorWithMovie>(context, 0, actors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.actor_card, parent, false)
        }

        val actorWithMovie = getItem(position)
        if (actorWithMovie != null) {
            // Set actor basic info
            val nameText = view.findViewById<TextView>(R.id.actor_name_card)
            val birthYearText = view.findViewById<TextView>(R.id.actor_birth_year_card)
            val nationalityText = view.findViewById<TextView>(R.id.actor_nationality_card)
            val professionText = view.findViewById<TextView>(R.id.actor_profession_card)
            val bioText = view.findViewById<TextView>(R.id.actor_bio_card)
            val deleteBtn = view.findViewById<ImageButton>(R.id.actor_delete_btn)

            nameText.text = actorWithMovie.actor.name
            birthYearText.text = actorWithMovie.actor.birthYear.toString()
            nationalityText.text = actorWithMovie.actor.nationality
            professionText.text = actorWithMovie.actor.profession
            bioText.text = actorWithMovie.actor.bio

            // Delete button
            deleteBtn.setOnClickListener {
                onDeleteClick(actorWithMovie)
            }

            // Movies container
            val moviesContainer = view.findViewById<LinearLayout>(R.id.movies_container)
            val noMoviesText = view.findViewById<TextView>(R.id.no_movies_text)

            moviesContainer.removeAllViews()

            if (actorWithMovie.movies.isEmpty()) {
                noMoviesText.visibility = View.VISIBLE
            } else {
                noMoviesText.visibility = View.GONE
                for (movie in actorWithMovie.movies) {
                    val movieView = LayoutInflater.from(context).inflate(R.layout.movie_item_in_actor, null)

                    val movieTitleText = movieView.findViewById<TextView>(R.id.movie_title_in_actor)
                    val movieGenreText = movieView.findViewById<TextView>(R.id.movie_genre_in_actor)
                    val movieYearText = movieView.findViewById<TextView>(R.id.movie_year_in_actor)
                    val movieDirectorText = movieView.findViewById<TextView>(R.id.movie_director_in_actor)

                    movieTitleText.text = movie.title
                    movieGenreText.text = movie.genre
                    movieYearText.text = movie.releaseYear.toString()
                    movieDirectorText.text = movie.director

                    moviesContainer.addView(movieView)
                }
            }
        }

        return view
    }
}

