package ru.itis.androidhomework.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ChartState())
    val uiState: StateFlow<ChartState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ChatEffect>(replay = 0)
    val effects = _effects.asSharedFlow()

    fun reduce(event: ChartScreenEvent) {
        when (event) {
            is ChartScreenEvent.CountInput -> {
                _uiState.update { it.copy(count = event.count) }
            }

            is ChartScreenEvent.InputCounts -> {
                _uiState.update { it.copy(counts = event.counts) }
            }

            is ChartScreenEvent.SubmitData -> {
                _uiState.update { it.copy(isValid = true) }

                val count = _uiState.value.count.toIntOrNull()
                val values = _uiState.value.counts.split(",")
                    .map { it.trim().toFloatOrNull() }

                val isValid = count != null && count > 0 &&
                        values.size == count && values.all { it != null && it >= 0f }

                val floatValue = values.mapNotNull { it }

                _uiState.update {
                    it.copy(
                        graphData = if (isValid) floatValue else emptyList(),
                        isValid = isValid,
                    )
                }

                viewModelScope.launch {
                    if (!isValid) {
                        _effects.emit(ChatEffect.ShowError)
                    }
                }
            }
        }
    }
}