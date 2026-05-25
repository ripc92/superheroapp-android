package pe.ripc.superheroapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.ripc.superheroapp.data.local.SuperheroDao
import pe.ripc.superheroapp.data.local.SuperheroDatabase
import pe.ripc.superheroapp.data.remote.SuperheroApi
import pe.ripc.superheroapp.data.repository.SuperheroRepositoryImpl
import pe.ripc.superheroapp.domain.repository.SuperheroRepository
import pe.ripc.superheroapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideSuperheroDatabase(
        @ApplicationContext context: Context
    ): SuperheroDatabase {
        return Room.databaseBuilder(
            context,
            SuperheroDatabase::class.java,
            "superhero.db"
        ).build()
    }

    @Provides
    fun provideSuperheroDao(database: SuperheroDatabase): SuperheroDao {
        return database.superheroDao()
    }

    @Provides
    @Singleton
    fun provideSuperheroRepository(
        api: SuperheroApi,
        superheroDao: SuperheroDao
    ): SuperheroRepository {
        return SuperheroRepositoryImpl(api, superheroDao, BuildConfig.SUPERHERO_API_TOKEN)
    }
}
