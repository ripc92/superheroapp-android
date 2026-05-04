package pe.ripc.superheroapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.ripc.superheroapp.data.remote.SuperheroApi
import pe.ripc.superheroapp.data.repository.SuperheroRepositoryImpl
import pe.ripc.superheroapp.domain.repository.SuperheroRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val SUPERHERO_API_TOKEN = "5b3dda14cd31b7bf4da2045ce95adc90"

    @Provides
    @Singleton
    fun provideSuperheroApi(): SuperheroApi {
        return Retrofit.Builder()
            .baseUrl(SuperheroApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SuperheroApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSuperheroRepository(api: SuperheroApi): SuperheroRepository {
        return SuperheroRepositoryImpl(api, SUPERHERO_API_TOKEN)
    }
}
