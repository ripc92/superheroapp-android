package pe.ripc.superheroapp.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.repository.SuperheroRepository

class GetSuperheroesUseCaseTest {

    private val repository = mockk<SuperheroRepository>()
    private val getSuperheroesUseCase = GetSuperheroesUseCase(repository)

    @Test
    fun `when search succeeds then return list of superheroes`() = runTest {
        // Given
        val superheroes = listOf(
            mockk<Superhero>(),
            mockk<Superhero>()
        )
        coEvery { repository.searchSuperheroes("a") } returns Result.success(superheroes)

        // When
        val result = getSuperheroesUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(superheroes, result.getOrNull())
    }

    @Test
    fun `when search fails then return failure`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { repository.searchSuperheroes("a") } returns Result.failure(exception)

        // When
        val result = getSuperheroesUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
