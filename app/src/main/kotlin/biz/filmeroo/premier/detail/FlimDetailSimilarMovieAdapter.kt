package biz.filmeroo.premier.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import biz.filmeroo.premier.api.FilmService
import biz.filmeroo.premier.api.Genres
import biz.filmeroo.premier.api.SimilarMovies
import biz.filmeroo.premier.databinding.SimilarMovieAdapBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import javax.inject.Inject

class FlimDetailSimilarMovieAdapter @Inject constructor(private val picasso: Picasso) : RecyclerView.Adapter<FlimDetailSimilarMovieAdapter.ViewHolder>() {
    private var data: List<SimilarMovies>? = null
    private var genres: List<Genres>? = null
    fun updateData(newData: List<SimilarMovies>, newGeneres: List<Genres>?) {
        data = newData
        genres = newGeneres
        Log.e("venkat", "genres_size= ${genres?.size}")
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val view: SimilarMovieAdapBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: SimilarMovies) {
            view.similarMovieTitle.text = item.title

            // TODO:check how to find the genre based on the ids
            val genresList = genres?.filter { item.genreIds.contains(it.id) }
            val genreNames = genresList?.joinToString(separator = " . ") { it.name }
            genreNames?.let {
                view.similarMovieSubtitle.text = genreNames
                view.similarMovieSubtitle.visibility = View.VISIBLE
            } ?: kotlin.run { view.similarMovieSubtitle.visibility = View.GONE }

            view.similarMovieRating.text = item.vote_average.toString()
            item.posterPath?.let {
                picasso
                    .load(FilmService.buildImageUrl(it))
                    .transform(RoundedCornersTransformation(40, 0))
                    .into(view.similarMovieImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SimilarMovieAdapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0
}
