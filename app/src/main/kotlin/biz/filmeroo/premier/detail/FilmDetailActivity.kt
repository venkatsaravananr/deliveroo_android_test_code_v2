package biz.filmeroo.premier.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.FilmService
import biz.filmeroo.premier.api.Genres
import biz.filmeroo.premier.api.SimilarMovies
import biz.filmeroo.premier.databinding.ActivityDetailBinding
import biz.filmeroo.premier.detail.FilmDetailViewModel.FilmDetailState
import biz.filmeroo.premier.detail.FilmDetailViewModel.SimilarMovieState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilmDetailActivity : AppCompatActivity() {

    @Inject
    internal lateinit var picasso: Picasso

    private lateinit var binding: ActivityDetailBinding

    private val filmDetailViewModel by viewModels<FilmDetailViewModel>()

    @Inject internal lateinit var similarMovieAdapter: FlimDetailSimilarMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        filmDetailViewModel.filmDetailState.observe(this, ::updateState)
        filmDetailViewModel.similarMoviesState.observe(this, ::updateSimilarMovieState)
    }

    private fun updateState(filmDetailState: FilmDetailState) {
        when (filmDetailState) {
            is FilmDetailState.Success -> displayMovie(filmDetailState.film)
            FilmDetailState.Error -> displayError()
        }
    }

    private fun updateSimilarMovieState(similarMovieState: SimilarMovieState) {
        when (similarMovieState) {
            is SimilarMovieState.Success -> displaySimilarMovies(similarMovieState.similarMoviesResponse.results)
            SimilarMovieState.Error -> displayError()
        }
    }

    private fun displayMovie(movie: ApiFilm) {
        if (movie.backdropPath != null) {
            picasso.load(FilmService.buildImageUrl(movie.backdropPath)).into(binding.filmImage)
        }
        binding.filmTitle.text = movie.title
        binding.filmOverview.text = movie.overview
    }

    private fun displayError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show()
    }

    private fun displaySimilarMovies(similarMovies: List<SimilarMovies>) {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.horizontalRecyclerView.layoutManager = linearLayoutManager
        binding.horizontalRecyclerView.adapter = similarMovieAdapter

        var generes: List<Genres>? = null
        if (filmDetailViewModel.filmDetailState.value is FilmDetailState.Success) {
            val response = filmDetailViewModel.filmDetailState.value as FilmDetailState.Success
            generes = response.film.genreIds
        }
        similarMovieAdapter.updateData(similarMovies, generes)
    }

    companion object {
        fun start(origin: Activity, id: Long) {
            origin.startActivity(
                Intent(origin, FilmDetailActivity::class.java).apply {
                    putExtra(FilmDetailViewModel.FILM_ID, id)
                }
            )
        }
    }
}
