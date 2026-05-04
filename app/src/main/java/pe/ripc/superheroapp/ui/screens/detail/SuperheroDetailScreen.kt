package pe.ripc.superheroapp.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import pe.ripc.superheroapp.domain.model.Superhero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroDetailScreen(
    superheroId: String,
    onBackClick: () -> Unit,
    viewModel: SuperheroDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is SuperheroDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SuperheroDetailUiState.Success -> {
                    SuperheroDetailContent(state.superhero)
                }
                is SuperheroDetailUiState.Error -> {
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
fun SuperheroDetailContent(superhero: Superhero) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = superhero.imageUrl,
            contentDescription = superhero.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        PaddingValues(16.dp).let {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = superhero.name,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Full Name: ${superhero.biography.fullName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Powerstats:", style = MaterialTheme.typography.titleMedium)
                Text(text = "Intelligence: ${superhero.powerstats.intelligence}")
                Text(text = "Strength: ${superhero.powerstats.strength}")
                Text(text = "Speed: ${superhero.powerstats.speed}")
                Text(text = "Durability: ${superhero.powerstats.durability}")
                Text(text = "Power: ${superhero.powerstats.power}")
                Text(text = "Combat: ${superhero.powerstats.combat}")
            }
        }
    }
}
