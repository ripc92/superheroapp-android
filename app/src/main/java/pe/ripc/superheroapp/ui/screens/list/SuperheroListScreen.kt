package pe.ripc.superheroapp.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import pe.ripc.superheroapp.domain.model.Superhero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroListScreen(
    onSuperheroClick: (String) -> Unit,
    viewModel: SuperheroListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Superheroes") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is SuperheroListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SuperheroListUiState.Success -> {
                    SuperheroList(state.superheroes, onSuperheroClick)
                }
                is SuperheroListUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun SuperheroList(
    superheroes: List<Superhero>,
    onSuperheroClick: (String) -> Unit
) {
    LazyColumn {
        items(superheroes) { superhero ->
            SuperheroItem(superhero, onSuperheroClick)
        }
    }
}

@Composable
fun SuperheroItem(
    superhero: Superhero,
    onSuperheroClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSuperheroClick(superhero.id) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = superhero.imageUrl,
                contentDescription = superhero.name,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = superhero.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
