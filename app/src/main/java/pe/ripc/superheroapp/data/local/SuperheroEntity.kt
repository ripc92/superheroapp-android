package pe.ripc.superheroapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import pe.ripc.superheroapp.domain.model.Biography
import pe.ripc.superheroapp.domain.model.Powerstats
import pe.ripc.superheroapp.domain.model.Superhero

@Entity(tableName = "superheroes")
data class SuperheroEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String,
    val fullName: String,
    val alterEgos: String,
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)

fun SuperheroEntity.toSuperhero(): Superhero {
    return Superhero(
        id = id,
        name = name,
        imageUrl = imageUrl,
        powerstats = Powerstats(
            intelligence = intelligence,
            strength = strength,
            speed = speed,
            durability = durability,
            power = power,
            combat = combat
        ),
        biography = Biography(
            fullName = fullName,
            alterEgos = alterEgos,
            firstAppearance = firstAppearance,
            publisher = publisher,
            alignment = alignment
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
        firstAppearance = biography.firstAppearance,
        publisher = biography.publisher,
        alignment = biography.alignment
    )
}
