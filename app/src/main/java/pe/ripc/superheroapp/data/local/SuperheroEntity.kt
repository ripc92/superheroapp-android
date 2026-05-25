package pe.ripc.superheroapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import pe.ripc.superheroapp.domain.model.*

@Entity(tableName = "superheroes")
data class SuperheroEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    // Powerstats
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String,
    // Biography
    val fullName: String,
    val alterEgos: String,
    val aliases: String, 
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String,
    val alignment: String,
    // Appearance
    val gender: String,
    val race: String,
    val height: String,
    val weight: String,
    val eyeColor: String,
    val hairColor: String,
    // Work
    val occupation: String,
    val base: String,
    // Connections
    val groupAffiliation: String,
    val relatives: String // Guardado con separador ;;
)

fun SuperheroEntity.toSuperhero(): Superhero {
    return Superhero(
        id = id,
        name = name,
        imageUrl = imageUrl,
        powerstats = Powerstats(intelligence, strength, speed, durability, power, combat),
        biography = Biography(
            fullName, alterEgos, aliases.split(",").filter { it.isNotBlank() },
            placeOfBirth, firstAppearance, publisher, alignment
        ),
        appearance = Appearance(gender, race, height, weight, eyeColor, hairColor),
        work = Work(occupation, base),
        connections = Connections(
            groupAffiliation, 
            relatives.split(";;").filter { it.isNotBlank() }
        )
    )
}

fun Superhero.toEntity(): SuperheroEntity {
    return SuperheroEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        intelligence = powerstats.intelligence,
        strength = powerstats.strength,
        speed = powerstats.speed,
        durability = powerstats.durability,
        power = powerstats.power,
        combat = powerstats.combat,
        fullName = biography.fullName,
        alterEgos = biography.alterEgos,
        aliases = biography.aliases.joinToString(","),
        placeOfBirth = biography.placeOfBirth,
        firstAppearance = biography.firstAppearance,
        publisher = biography.publisher,
        alignment = biography.alignment,
        gender = appearance.gender,
        race = appearance.race,
        height = appearance.height,
        weight = appearance.weight,
        eyeColor = appearance.eyeColor,
        hairColor = appearance.hairColor,
        occupation = work.occupation,
        base = work.base,
        groupAffiliation = connections.groupAffiliation,
        relatives = connections.relatives.joinToString(";;")
    )
}
