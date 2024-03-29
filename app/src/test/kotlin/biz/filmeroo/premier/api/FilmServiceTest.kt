package biz.filmeroo.premier.api

import com.nhaarman.mockito_kotlin.any
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FilmServiceTest {

    @Mock
    lateinit var flimService: FilmService

    @Mock
    lateinit var similarMoviesResponse: SimilarMoviesResponse

    @Test
    fun `formats image url`() {
        val formattedUrl = FilmService.buildImageUrl("/test.png")

        assertThat(formattedUrl).isEqualTo("https://image.tmdb.org/t/p/w500/test.png")
    }

    @Test
    fun `fetch similar movies - success`() {
        Mockito.`when`(flimService.similarMovies(any())).thenReturn(Single.just(similarMoviesResponse))
        val testObserver = flimService.similarMovies(any()).test()
        testObserver.assertNoErrors()
        testObserver.dispose()
    }
}
