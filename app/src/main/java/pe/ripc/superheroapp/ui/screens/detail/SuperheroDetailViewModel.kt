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

    private fun fetchSuperheroDetails() {
        viewModelScope.launch {
            _uiState.value = SuperheroDetailUiState.Loading
            getSuperheroByIdUseCase(superheroId).onSuccess { superhero ->
                _uiState.value = SuperheroDetailUiState.Success(superhero)
            }.onFailure {
                _uiState.value = SuperheroDetailUiState.Error(it.message ?: "Unknown error")
            }
        }
    }
}

sealed interface SuperheroDetailUiState {
    data object Loading : SuperheroDetailUiState
    data class Success(val superhero: Superhero) : SuperheroDetailUiState
    data class Error(val message: String) : SuperheroDetailUiState
}
