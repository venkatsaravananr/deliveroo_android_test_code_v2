package biz.filmeroo.premier.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.SimilarMoviesResponse
import biz.filmeroo.premier.base.BaseViewModel
import biz.filmeroo.premier.main.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
internal class FilmDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    filmRepository: FilmRepository
) : BaseViewModel() {

    private val filmId: Long = savedStateHandle.get<Long>(FILM_ID)!!
    private val _filmDetailState = MutableLiveData<FilmDetailState>()
    private val _similarMoviesState = MutableLiveData<SimilarMovieState>()

    val filmDetailState: LiveData<FilmDetailState>
        get() = _filmDetailState
    val similarMoviesState: LiveData<SimilarMovieState>
        get() = _similarMoviesState

    init {
        addSubscription(
            filmRepository.fetchMovie(filmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _filmDetailState.value = FilmDetailState.Success(it) },
                    { _filmDetailState.value = FilmDetailState.Error }
                )
        )

        addSubscription(
            filmRepository.fetchSimilarMovies(filmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _similarMoviesState.value = SimilarMovieState.Success(it) },
                    { _similarMoviesState.value = SimilarMovieState.Error }
                )
        )
    }

    internal companion object {
        internal const val FILM_ID = "filmId"
    }

    sealed interface FilmDetailState {
        data class Success(val film: ApiFilm) : FilmDetailState
        object Error : FilmDetailState
    }

    sealed interface SimilarMovieState {
        data class Success(val similarMoviesResponse: SimilarMoviesResponse) : SimilarMovieState
        object Error : SimilarMovieState
    }
}
