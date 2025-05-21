package ru.itis.androidhomework.presentation.screens.DetailsInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.androidhomework.exeption.runCatching
import ru.itis.androidhomework.domain.model.FeatureDetailsModel
import ru.itis.androidhomework.domain.usecase.GetFeatureDetailsUseCase
import ru.itis.androidhomework.exeption.ExceptionHandlerDelegate

class FeatureDetailsViewModel @AssistedInject constructor(
    @Assisted private val xid: String,
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val getFeatureDetails: GetFeatureDetailsUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : ViewModel() {

    private val _detailsState = MutableStateFlow(FeatureDetailsModel.EMPTY)
    val detailsState = _detailsState.asStateFlow()

    val errorsChannel = Channel<Throwable>()

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted xid: String,
            @Assisted savedStateHandle: SavedStateHandle
        ): FeatureDetailsViewModel
    }

    fun getInfoById() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getFeatureDetails.invoke(id = xid)
            }.onSuccess {
                _detailsState.value = it
            }.onFailure { throwable ->
                errorsChannel.send(throwable)
            }
        }
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}