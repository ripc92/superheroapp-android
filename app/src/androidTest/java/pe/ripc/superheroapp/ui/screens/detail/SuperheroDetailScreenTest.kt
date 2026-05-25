package pe.ripc.superheroapp.ui.screens.detail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pe.ripc.superheroapp.domain.model.Biography
import pe.ripc.superheroapp.domain.model.Powerstats
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.ui.theme.SuperheroAppTheme

class SuperheroDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeSuperhero = Superhero(
        id = "69",
        name = "Batman",
        imageUrl = "https://www.superherodb.com/pictures2/portraits/10/100/10441.jpg",
        powerstats = Powerstats("100", "26", "27", "50", "47", "100"),
        biography = Biography("Bruce Wayne", "No alter egos", "Detective Comics #27", "DC Comics", "good")
    )

    @Test
    fun superheroDetail_showsCorrectInformation() {
        composeTestRule.setContent {
            SuperheroAppTheme {
                SuperheroDetailContent(superhero = fakeSuperhero)
            }
        }

        // Verificar nombre principal
        composeTestRule.onNodeWithText("Batman").assertIsDisplayed()
        
        // Verificar información biográfica
        composeTestRule.onNodeWithText("Bruce Wayne").assertIsDisplayed()
        composeTestRule.onNodeWithText("DC Comics").assertIsDisplayed()

        // Verificar que se muestran etiquetas de estadísticas
        composeTestRule.onNodeWithText("Inteligencia").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fuerza").assertIsDisplayed()
        
        // Verificar un valor de estadística (Powerstats)
        composeTestRule.onNodeWithText("Velocidad").assertIsDisplayed()
        composeTestRule.onNodeWithText("Durabilidad").assertIsDisplayed()
    }
}
