package ru.itis.androidhomework.search

import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.androidhomework.domain.usecase.GetFeaturesUseCase
import ru.itis.androidhomework.exeption.ExceptionHandlerDelegate
import ru.itis.androidhomework.exeption.runCatching
import ru.itis.androidhomework.navigation.NavMain
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getFeaturesUseCase: GetFeaturesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val navMain: NavMain
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState: StateFlow<SearchScreenState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SearchEffect>(replay = 0)
    val effects = _effects.asSharedFlow()


    fun reduce(event: SearchScreenEvent){
        when(event) {

            is SearchScreenEvent.UserInput -> {
                _uiState.update { it.copy(input = event.input) }
            }

            is SearchScreenEvent.GetList -> {
                getList()
            }

            is SearchScreenEvent.OnItemClick -> {
                val isFeatureEnabled = Firebase.remoteConfig.getBoolean("is_feature_enabled")

                if (isFeatureEnabled) {
                    navMain.goToDetailPage(xid = event.xid)
                } else {
                    viewModelScope.launch {
                        _effects.emit(SearchEffect.ShowSnackbar)
                    }
                }
            }

            is SearchScreenEvent.OnButtonChartClick -> {
                navMain.goToChartPage()
            }
        }
    }

    private fun getList() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        isFocused = true,
                        isActionInProgress = true
                    )
                }
                getFeaturesUseCase.invoke(city = _uiState.value.input)
            }.onSuccess { featuresListModel ->
                _uiState.update {
                    it.copy(
                        list = featuresListModel.features,
                        isDataLoaded = true
                    )
                }
                _effects.emit(SearchEffect.ShowToast(featuresListModel.source))
            }.onFailure { throwable ->
                _effects.emit(SearchEffect.ShowError(throwable))
            }.also {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isFocused = false,
                        isActionInProgress = false
                    )
                }
            }
        }
    }
}