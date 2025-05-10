package ru.itis.androidhomework.presentation.screens.MainList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.androidhomework.exeption.runCatching
import ru.itis.androidhomework.domain.model.DataSource
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.usecase.GetFeaturesUseCase
import ru.itis.androidhomework.exeption.ExceptionHandlerDelegate
import ru.itis.androidhomework.navigation.NavMain
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val getFeaturesUseCase: GetFeaturesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val navMain: NavMain
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

    private val _showToast = MutableSharedFlow<DataSource>(replay = 0)
    val showToast = _showToast.asSharedFlow()

    val errorsChannel = Channel<Throwable>()

    fun getCoordinates(query: String) {
        _loadingState.value = true
        _inputFocusState.value = true
        _isActionInProgress.value = true
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                _emptyState.value = false
                getFeaturesUseCase.invoke(city = query)
            }.onSuccess { featuresListModel ->
                featuresListModel.features.ifEmpty { _emptyState.value = true }
                _featuresListState.value = featuresListModel.features
                _showToast.emit(featuresListModel.source)
            }.onFailure { throwable ->
                errorsChannel.send(throwable)
            }.also {
                _loadingState.value = false
                _inputFocusState.value = false
                _isActionInProgress.value = false
            }
        }
    }

    fun goToFeatureDetails(xid: String) {
        navMain.goToDetailPage(xid = xid)
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}