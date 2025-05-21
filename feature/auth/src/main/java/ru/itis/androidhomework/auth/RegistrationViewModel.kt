package ru.itis.androidhomework.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.androidhomework.domain.usecase.RegisterUseCase
import ru.itis.androidhomework.navigation.NavMain
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val navMain: NavMain
) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Init)
    val registrationState = _registrationState.asStateFlow()

    fun registerUser(userName: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            try {
                registerUseCase.invoke(userName, email, password)
                _registrationState.value = RegistrationState.Success
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error
            }
        }
    }


    fun goToLoginPage() {
        navMain.goToLoginPage()
    }
}

sealed class RegistrationState {
    object Init : RegistrationState()
    object Success : RegistrationState()
    object Error : RegistrationState()
    object Loading : RegistrationState()
}
