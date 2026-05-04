package pe.ripc.superheroapp.data.repository

import pe.ripc.superheroapp.data.remote.SuperheroApi
import pe.ripc.superheroapp.data.remote.dto.toSuperhero
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.repository.SuperheroRepository

class SuperheroRepositoryImpl(
    private val api: SuperheroApi,
    private val token: String
) : SuperheroRepository {

    override suspend fun searchSuperheroes(name: String): Result<List<Superhero>> {
        return try {
            val response = api.searchSuperheroes(token, name)
            if (response.response == "success") {
                Result.success(response.results?.map { it.toSuperhero() } ?: emptyList())
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
            Result.success(dto.toSuperhero())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
