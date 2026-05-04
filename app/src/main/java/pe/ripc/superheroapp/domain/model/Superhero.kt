package pe.ripc.superheroapp.domain.model

data class Superhero(
    val id: String,
    val name: String,
    val imageUrl: String,
    val powerstats: Powerstats,
    val biography: Biography
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
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)
