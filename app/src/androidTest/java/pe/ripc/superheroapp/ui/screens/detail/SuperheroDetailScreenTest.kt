package pe.ripc.superheroapp.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pe.ripc.superheroapp.domain.model.*
import pe.ripc.superheroapp.ui.theme.SuperheroAppTheme

class SuperheroDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeSuperhero = Superhero(
        id = "1",
        name = "Batman",
        imageUrl = "",
        powerstats = Powerstats("100", "26", "27", "50", "47", "99"),
        biography = Biography("Bruce Wayne", "No alter egos", emptyList(), "Gotham", "Detective Comics #27", "DC Comics", "good"),
        appearance = Appearance("Male", "Human", "6'2", "210 lb", "blue", "black"),
        work = Work("Businessman", "Gotham"),
        connections = Connections("Batfamily", emptyList())
    )

    @Test
    fun superheroDetail_showsCorrectInformation() {
        composeTestRule.setContent {
            SuperheroAppTheme {
                // Envolvemos en un contenedor scrolleable para que performScrollTo funcione
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    SuperheroDetailContent(superhero = fakeSuperhero)
                }
            }
        }

        // Verificar nombre principal
        composeTestRule.onNodeWithText("Batman").assertIsDisplayed()
        
        // Verificar información biográfica
        composeTestRule.onNodeWithText("Bruce Wayne").assertIsDisplayed()
        
        // Hacer scroll para asegurar visibilidad de las estadísticas
        composeTestRule.onNodeWithText("Inteligencia").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("100").assertIsDisplayed()
        
        // Hacer scroll al final para ver el último valor
        composeTestRule.onNodeWithText("99").performScrollTo().assertIsDisplayed()
    }
}
