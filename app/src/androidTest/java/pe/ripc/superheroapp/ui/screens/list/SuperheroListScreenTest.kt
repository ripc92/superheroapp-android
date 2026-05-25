package pe.ripc.superheroapp.ui.screens.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pe.ripc.superheroapp.domain.model.*
import pe.ripc.superheroapp.ui.theme.SuperheroAppTheme

class SuperheroListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeSuperheroes = listOf(
        Superhero(
            id = "1",
            name = "Batman",
            imageUrl = "",
            powerstats = Powerstats("100", "26", "27", "50", "47", "100"),
            biography = Biography("Bruce Wayne", "No alter egos", emptyList(), "Gotham", "Detective Comics #27", "DC Comics", "good"),
            appearance = Appearance("Male", "Human", "6'2", "210 lb", "blue", "black"),
            work = Work("Businessman", "Gotham"),
            connections = Connections("Batfamily", emptyList())
        ),
        Superhero(
            id = "2",
            name = "Superman",
            imageUrl = "",
            powerstats = Powerstats("94", "100", "100", "100", "100", "64"),
            biography = Biography("Clark Kent", "No alter egos", emptyList(), "Krypton", "Action Comics #1", "DC Comics", "good"),
            appearance = Appearance("Male", "Kryptonian", "6'3", "235 lb", "blue", "black"),
            work = Work("Reporter", "Metropolis"),
            connections = Connections("Justice League", emptyList())
        )
    )

    @Test
    fun superheroList_showsItems() {
        composeTestRule.setContent {
            val listState = rememberLazyListState()
            val focusRequester = FocusRequester()
            SuperheroAppTheme {
                SuperheroList(
                    superheroes = fakeSuperheroes,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    listState = listState,
                    focusRequester = focusRequester,
                    onSuperheroClick = {},
                    onSearch = {},
                    onHideKeyboard = {},
                    onTextFieldBoundsChanged = {}
                )
            }
        }

        // Verificar que los nombres de los héroes son visibles
        composeTestRule.onNodeWithText("Batman").assertIsDisplayed()
        composeTestRule.onNodeWithText("Superman").assertIsDisplayed()
    }

    @Test
    fun superheroList_itemClick_triggersCallback() {
        var clickedId = ""
        composeTestRule.setContent {
            val listState = rememberLazyListState()
            val focusRequester = FocusRequester()
            SuperheroAppTheme {
                SuperheroList(
                    superheroes = fakeSuperheroes,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    listState = listState,
                    focusRequester = focusRequester,
                    onSuperheroClick = { clickedId = it },
                    onSearch = {},
                    onHideKeyboard = {},
                    onTextFieldBoundsChanged = {}
                )
            }
        }

        // Hacer clic en Batman
        composeTestRule.onNodeWithText("Batman").performClick()

        // Verificar que el ID capturado es "1"
        assert(clickedId == "1")
    }
}
