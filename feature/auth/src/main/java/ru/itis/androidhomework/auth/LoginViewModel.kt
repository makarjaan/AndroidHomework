package ru.itis.androidhomework.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.androidhomework.domain.model.UserModel
import ru.itis.androidhomework.domain.usecase.LoginUseCase
import ru.itis.androidhomework.navigation.NavMain
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val navMain: NavMain
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val user: UserModel? = loginUseCase.invoke(email, password)
                if (user != null) {
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Initial
    }


    fun goToMainPage() {
        navMain.goToSearchPage()
    }
}

sealed class LoginState {
    object Initial : LoginState()
    object Success : LoginState()
    object Error : LoginState()
    object Loading : LoginState()
}
