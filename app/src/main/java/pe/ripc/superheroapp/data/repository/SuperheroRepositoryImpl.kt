package pe.ripc.superheroapp.data.repository

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
}
