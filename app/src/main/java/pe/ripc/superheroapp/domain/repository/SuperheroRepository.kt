package pe.ripc.superheroapp.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.ripc.superheroapp.domain.model.Superhero

interface SuperheroRepository {
    suspend fun searchSuperheroes(name: String): Result<List<Superhero>>
    suspend fun getSuperheroById(id: String): Result<Superhero>
    fun getSuperheroStream(id: String): Flow<Result<Superhero>>
}
