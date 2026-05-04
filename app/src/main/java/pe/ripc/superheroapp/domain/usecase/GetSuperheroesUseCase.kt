package pe.ripc.superheroapp.domain.usecase

import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.repository.SuperheroRepository
import javax.inject.Inject

class GetSuperheroesUseCase @Inject constructor(
    private val repository: SuperheroRepository
) {
    suspend operator fun invoke(query: String = "a"): Result<List<Superhero>> {
        return repository.searchSuperheroes(query)
    }
}
