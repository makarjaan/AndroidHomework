package ru.itis.androidhomework.presentation.screens.MainList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.androidhomework.data.exeption.ExceptionHandlerDelegate
import ru.itis.androidhomework.data.exeption.runCatching
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.usecase.GetFeaturesUseCase
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val getFeaturesUseCase: GetFeaturesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
): ViewModel() {

    private val _featuresListState = MutableStateFlow<List<FeaturesModel>>(emptyList())
    val featureListState = _featuresListState.asStateFlow()

    private val _loadingState = MutableStateFlow(value = false)
    val loadingState = _loadingState.asStateFlow()

    private val _emptyState = MutableStateFlow(false)
    val emptyState = _emptyState.asStateFlow()

    private val _inputFocusState = MutableStateFlow(value = false)
    val inputFocusState = _inputFocusState.asStateFlow()

    private val _isActionInProgress = MutableStateFlow(false)
    val isActionInProgress = _isActionInProgress.asStateFlow()

    val errorsChannel = Channel<Throwable>()

    fun getCoordinates(query: String) {
        _loadingState.value = true
        _inputFocusState.value = true
        _isActionInProgress.value = true
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                Log.d("OKHTTP", "летсго")
                _emptyState.value = false
                getFeaturesUseCase.invoke(city = query)
            }.onSuccess { featuresListModel ->
                featuresListModel.ifEmpty { _emptyState.value = true }
                _featuresListState.value = featuresListModel
            }.onFailure { throwable ->
                errorsChannel.send(throwable)
            }.also {
                _loadingState.value = false
                _inputFocusState.value = false
                _isActionInProgress.value = false
            }
        }
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}