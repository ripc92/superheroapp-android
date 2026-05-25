package pe.ripc.superheroapp.domain.model

data class Superhero(
    val id: String,
    val name: String,
    val imageUrl: String,
    val powerstats: Powerstats,
    val biography: Biography,
    val appearance: Appearance,
    val work: Work,
    val connections: Connections
)

data class Powerstats(
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String
)

data class Biography(
    val fullName: String,
    val alterEgos: String,
    val aliases: List<String>,
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)

data class Appearance(
    val gender: String,
    val race: String,
    val height: String,
    val weight: String,
    val eyeColor: String,
    val hairColor: String
)

data class Work(
    val occupation: String,
    val base: String
)

data class Connections(
    val groupAffiliation: String,
    val relatives: List<String>
)
