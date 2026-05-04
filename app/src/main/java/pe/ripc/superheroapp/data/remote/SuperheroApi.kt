package pe.ripc.superheroapp.data.remote

import pe.ripc.superheroapp.data.remote.dto.SearchResponseDto
import pe.ripc.superheroapp.data.remote.dto.SuperheroDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SuperheroApi {
    @GET("{token}/search/{name}")
    suspend fun searchSuperheroes(
        @Path("token") token: String,
        @Path("name") name: String
    ): SearchResponseDto

    @GET("{token}/{id}")
    suspend fun getSuperheroDetails(
        @Path("token") token: String,
        @Path("id") id: String
    ): SuperheroDto

    companion object {
        const val BASE_URL = "https://superheroapi.com/api/"
    }
}
