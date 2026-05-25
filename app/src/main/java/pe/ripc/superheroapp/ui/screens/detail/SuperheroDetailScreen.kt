package pe.ripc.superheroapp.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
    val scrollState = rememberScrollState()
    val headerHeight = 350.dp
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (val state = uiState) {
            is SuperheroDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SuperheroDetailUiState.Success -> {
                // Background Image with Parallax
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                        .graphicsLayer {
                            translationY = -scrollState.value * 0.5f
                            alpha = (-1f / headerHeightPx) * scrollState.value + 1
                        }
                ) {
                    AsyncImage(
                        model = state.superhero.imageUrl,
                        contentDescription = state.superhero.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay to blend with background
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                                    startY = headerHeightPx * 0.6f
                                )
                            )
                    )
                }

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(headerHeight))
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    ) {
                        SuperheroDetailContent(state.superhero)
                    }
                }

                // Top Bar
                val alpha = (scrollState.value.toFloat() / headerHeightPx).coerceIn(0f, 1f)
                TopAppBar(
                    title = {
                        if (alpha > 0.8f) {
                            Text(
                                text = state.superhero.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 1f - alpha)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Atrás",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                // Botón flotante discreto de reintento si falló el refresco
                if (state.refreshError != null) {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.fetchSuperheroDetails() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp),
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                        text = { Text("Reintentar actualizar") }
                    )
                }
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

@Composable
fun SuperheroDetailContent(superhero: Superhero) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = superhero.name,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = superhero.biography.publisher,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Información Biográfica
        DetailCard(title = "Biografía") {
            DetailRow("Nombre real", superhero.biography.fullName)
            DetailRow("Alias", superhero.biography.aliases.joinToString(", ").ifEmpty { "Ninguno" })
            DetailRow("Lugar de nacimiento", superhero.biography.placeOfBirth)
            DetailRow("Primera aparición", superhero.biography.firstAppearance)
            DetailRow("Bando", superhero.biography.alignment.replaceFirstChar { it.uppercase() })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apariencia
        DetailCard(title = "Apariencia") {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    DetailRow("Género", superhero.appearance.gender)
                    DetailRow("Raza", superhero.appearance.race)
                }
                Column(modifier = Modifier.weight(1f)) {
                    DetailRow("Ojos", superhero.appearance.eyeColor)
                    DetailRow("Cabello", superhero.appearance.hairColor)
                }
            }
            DetailRow("Altura", superhero.appearance.height)
            DetailRow("Peso", superhero.appearance.weight)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estadísticas de Poder
        DetailCard(title = "Estadísticas de Poder") {
            PowerStatItem("Inteligencia", superhero.powerstats.intelligence)
            PowerStatItem("Fuerza", superhero.powerstats.strength)
            PowerStatItem("Velocidad", superhero.powerstats.speed)
            PowerStatItem("Durabilidad", superhero.powerstats.durability)
            PowerStatItem("Poder", superhero.powerstats.power)
            PowerStatItem("Combate", superhero.powerstats.combat)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trabajo y Conexiones
        DetailCard(title = "Trabajo y Conexiones") {
            DetailRow("Ocupación", superhero.work.occupation)
            DetailRow("Base", superhero.work.base)
            DetailRow("Grupos", superhero.connections.groupAffiliation)
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Familiares",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            if (superhero.connections.relatives.isEmpty()) {
                Text(text = "Ninguno", style = MaterialTheme.typography.bodyLarge)
            } else {
                superhero.connections.relatives.forEach { relative ->
                    Text(
                        text = "• $relative",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DetailCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PowerStatItem(label: String, value: String) {
    val statInt = value.toIntOrNull() ?: 0
    val progress = statInt / 100f
    val displayValue = if (value == "unknown" || value == "null") "0" else value

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = displayValue,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary // GoldenEnergy
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round
        )
    }
}
