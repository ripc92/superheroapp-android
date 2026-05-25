package pe.ripc.superheroapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.repository.SuperheroRepository
import javax.inject.Inject

class GetSuperheroByIdUseCase @Inject constructor(
    private val repository: SuperheroRepository
) {
    suspend operator fun invoke(id: String): Result<Superhero> {
        return repository.getSuperheroById(id)
    }

    fun stream(id: String): Flow<Result<Superhero>> {
        return repository.getSuperheroStream(id)
    }
}
