package com.example.lab8_plataformas.Ui.fragments.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8_plataformas.Data.datasource.model.dataCharacters
import com.example.lab8_plataformas.Data.datasource.util.Resource
import com.example.lab8_plataformas.Data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _screenState: MutableStateFlow<ListUiState> = MutableStateFlow(ListUiState.Empty)
    val screenState: StateFlow<ListUiState> = _screenState

    sealed interface ListUiState{
        object Empty: ListUiState
        object Loading: ListUiState
        class Success(val data: List<dataCharacters>): ListUiState
        class Error(val message: String): ListUiState
    }

    fun getCharacterList(){
        viewModelScope.launch {
            _screenState.value = ListUiState.Loading
            val response = repository.getAllCharacters()

            when (response){
                is Resource.Error -> {
                    _screenState.value = ListUiState.Error(
                        message = response.message ?: ""
                    )
                }
                is Resource.Success -> {
                    if (response.data.isNullOrEmpty()) {
                        _screenState.value = ListUiState.Empty
                    } else {
                        _screenState.value = ListUiState.Success(
                            data = response.data
                        )
                    }
                }
            }
        }
    }
}