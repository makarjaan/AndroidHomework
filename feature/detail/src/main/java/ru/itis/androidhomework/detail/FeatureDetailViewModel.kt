package ru.itis.androidhomework.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.androidhomework.domain.usecase.GetFeatureDetailsUseCase
import ru.itis.androidhomework.exeption.ExceptionHandlerDelegate
import ru.itis.androidhomework.exeption.runCatching
import javax.inject.Inject

@HiltViewModel
class FeatureDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFeatureDetails: GetFeatureDetailsUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeatureDetailState())
    val uiState: StateFlow<FeatureDetailState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<DetailEffect>(replay = 0)
    val effects = _effects.asSharedFlow()

    private val xid: String = savedStateHandle["xid"] ?: ""

    init {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                val result = getFeatureDetails.invoke(id = xid)
                _uiState.value = _uiState.value.copy(
                    image = result.image,
                    placeName = result.name,
                    placeAddress = result.address,
                    desc = result.text,
                    wikilink = result.wikipedia
                )
            }.onFailure { throwable ->
                _effects.emit(DetailEffect.ShowError(throwable))
            }
        }
    }
}