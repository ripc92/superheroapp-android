package pe.ripc.superheroapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.ripc.superheroapp.ui.screens.detail.SuperheroDetailScreen
import pe.ripc.superheroapp.ui.screens.list.SuperheroListScreen

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Detail : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            SuperheroListScreen(
                onSuperheroClick = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                }
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            SuperheroDetailScreen(
                superheroId = id,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
