package pe.ripc.superheroapp.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ripc.superheroapp.data.local.SuperheroDao
import pe.ripc.superheroapp.data.local.SuperheroEntity
import pe.ripc.superheroapp.data.remote.SuperheroApi
import pe.ripc.superheroapp.data.remote.dto.BiographyDto
import pe.ripc.superheroapp.data.remote.dto.ImageDto
import pe.ripc.superheroapp.data.remote.dto.PowerstatsDto
import pe.ripc.superheroapp.data.remote.dto.SearchResponseDto
import pe.ripc.superheroapp.data.remote.dto.SuperheroDto

class SuperheroRepositoryImplTest {

    private val api = mockk<SuperheroApi>()
    private val dao = mockk<SuperheroDao>(relaxed = true)
    private val repository = SuperheroRepositoryImpl(api, dao, TOKEN)

    @Test
    fun `when search succeeds then cache superheroes`() = runTest {
        // Given
        val dto = superheroDto(id = "1", name = "Batman")
        coEvery { dao.searchSuperheroes("bat") } returns emptyList()
        coEvery { api.searchSuperheroes(TOKEN, "bat") } returns SearchResponseDto(
            response = "success",
            results = listOf(dto)
        )

        // When
        val result = repository.searchSuperheroes("bat")

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Batman", result.getOrNull()?.first()?.name)
        coVerify {
            dao.upsertSuperheroes(match { superheroes ->
                superheroes.size == 1 && superheroes.first().id == "1"
            })
        }
    }

    @Test
    fun `when cached search has results then return cached superheroes`() = runTest {
        // Given
        val cachedSuperhero = superheroEntity(id = "2", name = "Superman")
        coEvery { dao.searchSuperheroes("super") } returns listOf(cachedSuperhero)

        // When
        val result = repository.searchSuperheroes("super")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf("Superman"), result.getOrNull()?.map { it.name })
        coVerify(exactly = 0) { api.searchSuperheroes(TOKEN, "super") }
    }

    @Test
    fun `when cached search is empty and api fails then return failure`() = runTest {
        // Given
        coEvery { dao.searchSuperheroes("super") } returns emptyList()
        coEvery { api.searchSuperheroes(TOKEN, "super") } throws Exception("Network error")

        // When
        val result = repository.searchSuperheroes("super")

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `when detail fails then return cached superhero`() = runTest {
        // Given
        val cachedSuperhero = superheroEntity(id = "3", name = "Wonder Woman")
        coEvery { api.getSuperheroDetails(TOKEN, "3") } throws Exception("Network error")
        coEvery { dao.getSuperheroById("3") } returns cachedSuperhero

        // When
        val result = repository.getSuperheroById("3")

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Wonder Woman", result.getOrNull()?.name)
    }

    private fun superheroDto(id: String, name: String): SuperheroDto {
        return SuperheroDto(
            id = id,
            name = name,
            powerstats = PowerstatsDto(
                intelligence = "100",
                strength = "90",
                speed = "80",
                durability = "70",
                power = "60",
                combat = "50"
            ),
            biography = BiographyDto(
                fullName = "$name Full",
                alterEgos = "No alter egos found.",
                firstAppearance = "Test Comics",
                publisher = "Test Publisher",
                alignment = "good"
            ),
            image = ImageDto(url = "https://example.com/$id.png")
        )
    }

    private fun superheroEntity(id: String, name: String): SuperheroEntity {
        return SuperheroEntity(
            id = id,
            name = name,
            imageUrl = "https://example.com/$id.png",
            intelligence = "100",
            strength = "90",
            speed = "80",
            durability = "70",
            power = "60",
            combat = "50",
            fullName = "$name Full",
            alterEgos = "No alter egos found.",
            firstAppearance = "Test Comics",
            publisher = "Test Publisher",
            alignment = "good"
        )
    }

    private companion object {
        const val TOKEN = "test-token"
    }
}
