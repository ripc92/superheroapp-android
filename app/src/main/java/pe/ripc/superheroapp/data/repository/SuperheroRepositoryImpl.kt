package pe.ripc.superheroapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pe.ripc.superheroapp.data.local.SuperheroDao
import pe.ripc.superheroapp.data.local.toEntity
import pe.ripc.superheroapp.data.local.toSuperhero
import pe.ripc.superheroapp.data.remote.SuperheroApi
import pe.ripc.superheroapp.data.remote.dto.toSuperhero
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.repository.SuperheroRepository

class SuperheroRepositoryImpl(
    private val api: SuperheroApi,
    private val superheroDao: SuperheroDao,
    private val token: String
) : SuperheroRepository {

    override suspend fun searchSuperheroes(name: String): Result<List<Superhero>> {
        val cachedSuperheroes = superheroDao.searchSuperheroes(name).map { it.toSuperhero() }
        if (cachedSuperheroes.isNotEmpty()) {
            return Result.success(cachedSuperheroes)
        }

        return try {
            val response = api.searchSuperheroes(token, name)
            if (response.response == "success") {
                val superheroes = response.results?.map { it.toSuperhero() } ?: emptyList()
                superheroDao.upsertSuperheroes(superheroes.map { it.toEntity() })
                Result.success(superheroes)
            } else {
                Result.failure(Exception("Error fetching superheroes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSuperheroById(id: String): Result<Superhero> {
        return try {
            val dto = api.getSuperheroDetails(token, id)
            val superhero = dto.toSuperhero()
            superheroDao.upsertSuperhero(superhero.toEntity())
            Result.success(superhero)
        } catch (e: Exception) {
            val cachedSuperhero = superheroDao.getSuperheroById(id)?.toSuperhero()
            if (cachedSuperhero != null) {
                Result.success(cachedSuperhero)
            } else {
                Result.failure(e)
            }
        }
    }

    override fun getSuperheroStream(id: String): Flow<Result<Superhero>> = flow {
        // 1. Intentar obtener de la base de datos primero
        val cached = superheroDao.getSuperheroById(id)?.toSuperhero()
        if (cached != null) {
            emit(Result.success(cached))
        }

        // 2. Intentar refrescar desde el API
        try {
            val dto = api.getSuperheroDetails(token, id)
            val superhero = dto.toSuperhero()
            superheroDao.upsertSuperhero(superhero.toEntity())
            emit(Result.success(superhero))
        } catch (e: Exception) {
            // Si falló el API pero ya emitimos caché, el ViewModel manejará el error discreto
            // Si NO había caché y falló el API, emitimos el error para mostrar pantalla de error
            if (cached == null) {
                emit(Result.failure(e))
            } else {
                // Ya emitimos éxito con caché, lanzamos la excepción para que el colector
                // (ViewModel) pueda detectarla si quiere mostrar el FAB de reintento.
                // En Kotlin Flows, podemos usar catch { } o envolver el error.
                // Vamos a emitir un tipo de error específico o simplemente re-emitir el fallo
                // pero el ViewModel sabrá qué hacer.
                emit(Result.failure(e))
            }
        }
    }
}
