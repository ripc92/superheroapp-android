package pe.ripc.superheroapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.usecase.GetSuperheroByIdUseCase
import javax.inject.Inject

@HiltViewModel
class SuperheroDetailViewModel @Inject constructor(
    private val getSuperheroByIdUseCase: GetSuperheroByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val superheroId: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow<SuperheroDetailUiState>(SuperheroDetailUiState.Loading)
    val uiState: StateFlow<SuperheroDetailUiState> = _uiState.asStateFlow()

    init {
        fetchSuperheroDetails()
    }

    fun fetchSuperheroDetails() {
        viewModelScope.launch {
            // Solo mostramos Loading si no tenemos datos ya (éxito previo)
            if (_uiState.value !is SuperheroDetailUiState.Success) {
                _uiState.value = SuperheroDetailUiState.Loading
            }
            
            getSuperheroByIdUseCase.stream(superheroId).collect { result ->
                result.onSuccess { superhero ->
                    _uiState.value = SuperheroDetailUiState.Success(
                        superhero = superhero,
                        isRefreshing = false,
                        refreshError = null
                    )
                }.onFailure { error ->
                    val currentState = _uiState.value
                    if (currentState is SuperheroDetailUiState.Success) {
                        // Si ya tenemos datos, solo notificamos el error de refresco para el FAB
                        _uiState.value = currentState.copy(
                            refreshError = error.message ?: "Error al actualizar datos"
                        )
                    } else {
                        // Si no hay datos, mostramos pantalla de error completa
                        _uiState.value = SuperheroDetailUiState.Error(error.message ?: "Error de conexión")
                    }
                }
            }
        }
    }
}

sealed interface SuperheroDetailUiState {
    data object Loading : SuperheroDetailUiState
    data class Success(
        val superhero: Superhero,
        val isRefreshing: Boolean = false,
        val refreshError: String? = null
    ) : SuperheroDetailUiState
    data class Error(val message: String) : SuperheroDetailUiState
}
