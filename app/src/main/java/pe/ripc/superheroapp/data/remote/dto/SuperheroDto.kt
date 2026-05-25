package pe.ripc.superheroapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import pe.ripc.superheroapp.domain.model.*

data class SuperheroDto(
    val id: String,
    val name: String,
    val powerstats: PowerstatsDto,
    val biography: BiographyDto,
    val appearance: AppearanceDto,
    val work: WorkDto,
    val connections: ConnectionsDto,
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
    val aliases: List<String>?,
    @SerializedName("place-of-birth") val placeOfBirth: String?,
    @SerializedName("first-appearance") val firstAppearance: String?,
    val publisher: String?,
    val alignment: String?
)

data class AppearanceDto(
    val gender: String?,
    val race: String?,
    val height: List<String>?,
    val weight: List<String>?,
    @SerializedName("eye-color") val eyeColor: String?,
    @SerializedName("hair-color") val hairColor: String?
)

data class WorkDto(
    val occupation: String?,
    val base: String?
)

data class ConnectionsDto(
    @SerializedName("group-affiliation") val groupAffiliation: String?,
    val relatives: String?
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
            aliases = biography.aliases ?: emptyList(),
            placeOfBirth = biography.placeOfBirth ?: "unknown",
            firstAppearance = biography.firstAppearance ?: "unknown",
            publisher = biography.publisher ?: "unknown",
            alignment = biography.alignment ?: "unknown"
        ),
        appearance = Appearance(
            gender = appearance.gender ?: "unknown",
            race = appearance.race ?: "unknown",
            height = appearance.height?.joinToString(" / ") ?: "unknown",
            weight = appearance.weight?.joinToString(" / ") ?: "unknown",
            eyeColor = appearance.eyeColor ?: "unknown",
            hairColor = appearance.hairColor ?: "unknown"
        ),
        work = Work(
            occupation = work.occupation ?: "unknown",
            base = work.base ?: "unknown"
        ),
        connections = Connections(
            groupAffiliation = connections.groupAffiliation ?: "unknown",
            relatives = parseRelatives(connections.relatives)
        )
    )
}

private fun parseRelatives(relatives: String?): List<String> {
    if (relatives == null || relatives == "-" || relatives.lowercase() == "no relatives found") {
        return emptyList()
    }

    val result = mutableListOf<String>()
    var current = StringBuilder()
    var depth = 0

    for (char in relatives) {
        when (char) {
            '(' -> {
                depth++
                current.append(char)
            }
            ')' -> {
                depth--
                current.append(char)
            }
            ',', ';' -> {
                if (depth == 0) {
                    val entry = current.toString().trim()
                    if (entry.isNotBlank()) result.add(entry)
                    current = StringBuilder()
                } else {
                    current.append(char)
                }
            }
            else -> current.append(char)
        }
    }

    val lastEntry = current.toString().trim()
    if (lastEntry.isNotBlank()) result.add(lastEntry)

    return result
}
