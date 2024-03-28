package biz.filmeroo.premier.app

import android.content.Context
import android.content.res.Resources
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.FilmService
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideFilmService(client: OkHttpClient): FilmService {
        return Retrofit.Builder()
            .baseUrl(FilmService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(FilmService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(resources: Resources): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createAuthInterceptor(resources))
            .build()
    }

    private fun createAuthInterceptor(resources: Resources): Interceptor {
        return Interceptor { chain ->
            val updatedUrl = chain.request().url().newBuilder()
                .addQueryParameter(FilmService.API_KEY_PARAM, resources.getString(R.string.api_key))
                .build()
            chain.proceed(
                chain.request().newBuilder()
                    .url(updatedUrl)
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    fun providePicasso(): Picasso = Picasso.get()
}
