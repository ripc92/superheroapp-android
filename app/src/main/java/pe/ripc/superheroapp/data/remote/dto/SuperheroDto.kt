package pe.ripc.superheroapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import pe.ripc.superheroapp.domain.model.Biography
import pe.ripc.superheroapp.domain.model.Powerstats
import pe.ripc.superheroapp.domain.model.Superhero

data class SuperheroDto(
    val id: String,
    val name: String,
    val powerstats: PowerstatsDto,
    val biography: BiographyDto,
    val image: ImageDto
)

data class PowerstatsDto(
    val intelligence: String?,
    val strength: String?,
    val speed: String?,
    val durability: String?,
    val power: String?,
    val combat: String?
)

data class BiographyDto(
    @SerializedName("full-name") val fullName: String?,
    @SerializedName("alter-egos") val alterEgos: String?,
    @SerializedName("first-appearance") val firstAppearance: String?,
    val publisher: String?,
    val alignment: String?
)

data class ImageDto(
    val url: String
)

fun SuperheroDto.toSuperhero(): Superhero {
    return Superhero(
        id = id,
        name = name,
        imageUrl = image.url,
        powerstats = Powerstats(
            intelligence = powerstats.intelligence ?: "unknown",
            strength = powerstats.strength ?: "unknown",
            speed = powerstats.speed ?: "unknown",
            durability = powerstats.durability ?: "unknown",
            power = powerstats.power ?: "unknown",
            combat = powerstats.combat ?: "unknown"
        ),
        biography = Biography(
            fullName = biography.fullName ?: "unknown",
            alterEgos = biography.alterEgos ?: "unknown",
            firstAppearance = biography.firstAppearance ?: "unknown",
            publisher = biography.publisher ?: "unknown",
            alignment = biography.alignment ?: "unknown"
        )
    )
}
