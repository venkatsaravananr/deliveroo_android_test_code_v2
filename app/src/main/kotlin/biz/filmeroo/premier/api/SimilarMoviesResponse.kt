package biz.filmeroo.premier.api

import com.google.gson.annotations.SerializedName

data class SimilarMoviesResponse(
    val page: Double,
    val results: List<SimilarMovies>
)

data class SimilarMovies(
    val adult: Boolean,
    val backdrop_path: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    val id: Double,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Double
)
