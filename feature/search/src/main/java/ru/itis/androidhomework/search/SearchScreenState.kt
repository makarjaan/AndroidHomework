package ru.itis.androidhomework.search

import androidx.compose.runtime.Immutable
import ru.itis.androidhomework.domain.model.DataSource
import ru.itis.androidhomework.domain.model.FeaturesModel

@Immutable
data class SearchScreenState (
    val list: List<FeaturesModel> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val isFocused: Boolean = false,
    val isActionInProgress: Boolean = false,
    val isDataLoaded: Boolean = false,
)

sealed interface SearchScreenEvent {
    object GetList: SearchScreenEvent
    object OnButtonChartClick: SearchScreenEvent
    data class OnItemClick(val xid: String) : SearchScreenEvent
    data class UserInput(val input: String) : SearchScreenEvent
}

sealed interface SearchEffect {
    data class ShowToast(val source: DataSource) : SearchEffect
    data class ShowError(val throwable: Throwable) : SearchEffect
    object ShowSnackbar: SearchEffect
}
