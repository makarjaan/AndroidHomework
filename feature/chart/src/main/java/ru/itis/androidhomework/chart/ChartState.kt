package ru.itis.androidhomework.chart

import androidx.compose.runtime.Immutable

@Immutable
data class ChartState (
    val count: String = "",
    val counts: String = "",
    val isValid: Boolean = false,
    val graphData: List<Float> = emptyList(),
    val error: Boolean = false,
)

sealed interface ChartScreenEvent {
    data class CountInput(val count: String) : ChartScreenEvent
    data class InputCounts(val counts: String) : ChartScreenEvent
    object SubmitData: ChartScreenEvent
}


sealed interface ChatEffect {
    object ShowError : ChatEffect
}