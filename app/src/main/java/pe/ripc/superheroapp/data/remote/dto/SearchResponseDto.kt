package pe.ripc.superheroapp.data.remote.dto

data class SearchResponseDto(
    val response: String,
    val results: List<SuperheroDto>?
)
