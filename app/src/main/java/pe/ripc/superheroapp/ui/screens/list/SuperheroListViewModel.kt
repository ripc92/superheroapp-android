package pe.ripc.superheroapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.ripc.superheroapp.domain.model.Superhero
import pe.ripc.superheroapp.domain.usecase.GetSuperheroesUseCase
import javax.inject.Inject

@HiltViewModel
class SuperheroListViewModel @Inject constructor(
    private val getSuperheroesUseCase: GetSuperheroesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SuperheroListUiState>(SuperheroListUiState.Loading)
    val uiState: StateFlow<SuperheroListUiState> = _uiState.asStateFlow()

    init {
        fetchSuperheroes()
    }

    fun fetchSuperheroes(query: String = "a") {
        viewModelScope.launch {
            _uiState.value = SuperheroListUiState.Loading
            getSuperheroesUseCase(query).onSuccess { list ->
                _uiState.value = SuperheroListUiState.Success(list)
            }.onFailure {
                _uiState.value = SuperheroListUiState.Error(it.message ?: "Unknown error")
            }
        }
    }
}

sealed interface SuperheroListUiState {
    data object Loading : SuperheroListUiState
    data class Success(val superheroes: List<Superhero>) : SuperheroListUiState
    data class Error(val message: String) : SuperheroListUiState
}
