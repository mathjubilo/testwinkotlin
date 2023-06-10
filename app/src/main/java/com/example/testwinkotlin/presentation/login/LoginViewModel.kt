package com.example.testwinkotlin.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwinkotlin.data.local.api.BiometricResponse
import com.example.testwinkotlin.data.local.api.BiometricsApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.domain.model.TokensAndCredentialsModel
import com.example.testwinkotlin.utils.AsyncTaskResponse
import com.example.testwinkotlin.domain.model.toJson
import com.example.testwinkotlin.domain.useCases.DoLoginUC
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenEvents
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val doLoginUseCase: DoLoginUC
): ViewModel() {

    var state by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var tokensAndCredentials = TokensAndCredentialsModel("","","","")
    var errorMessage = ""

    init {

        setEvent(initialEvent)
    }

    fun setEvent(event: String) {

        Log.d("LoginViewModel","Running event ${event}")

        state = LoginViewState.loading

        when (event) {

            LoginViewEvent.showLoginForm -> {

                if (LaunchScreenViewModel.biometricLogin && !IWinApi.USERNAME.isEmpty() && !IWinApi.PASSWORD.isEmpty()) {
                    Log.d("LoginViewModel", "Showing Biometric Prompt")
                    LaunchScreenViewModel.initialEvent = LaunchScreenEvents.loginWithBiometrics
                    setEvent(LoginViewEvent.navigateToLaunchScreenView)
                } else {
                    state = LoginViewState.loginForm
                }
            }

            LoginViewEvent.doLogin -> {

                doLogin()
            }

            LoginViewEvent.loginSuccess -> {

                LaunchScreenViewModel.stringToEncrypt = tokensAndCredentials.toJson()
                println("kork loginSuccess stringtoencrypt ${LaunchScreenViewModel.stringToEncrypt}")
                LaunchScreenViewModel.initialEvent = LaunchScreenEvents.encryptTokensAndCredentials
                setEvent(LoginViewEvent.navigateToLaunchScreenView)
            }

            LoginViewEvent.loginError -> {

                setEvent(LoginViewEvent.showErrorMessage)
            }

            LoginViewEvent.navigateToAppView -> {

                state = LoginViewState.navigateToAppView
            }

            LoginViewEvent.navigateToLaunchScreenView -> {

                state = LoginViewState.navigateToLaunchScreenView
            }

            LoginViewEvent.showErrorMessage -> {

                state = LoginViewState.error
            }
        }
    }

    private fun doLogin() {
        if (username.isEmpty() && password.isEmpty() && LaunchScreenViewModel.biometricLogin && !IWinApi.USERNAME.isEmpty() && !IWinApi.PASSWORD.isEmpty()) {
            username = IWinApi.USERNAME
            password = IWinApi.PASSWORD
        }
        if (!username.isEmpty() && !password.isEmpty()) {

            viewModelScope.launch {

                doLoginUseCase.invoke(username, password).collect { result ->

                    when (result) {

                        is AsyncTaskResponse.Success -> {

                            result.data?.let { tokens ->

                                if (!tokens.accessToken.isEmpty()) {

                                    IWinApi.USERNAME = tokens.username
                                    IWinApi.ACCESS_TOKEN = tokens.accessToken
                                    IWinApi.REFRESH_TOKEN = tokens.refreshToken

                                    println("kork received tokens ${tokens.accessToken}")
                                    tokensAndCredentials = tokens
                                    setEvent(LoginViewEvent.loginSuccess)
                                }
                            }
                        }

                        is AsyncTaskResponse.Error -> {

                            errorMessage = "Ha ocurrido un error " + result.message
                            setEvent(LoginViewEvent.showErrorMessage)
                        }
                    }
                }
            }
        } else {

            errorMessage = "Campos vacíos"
            setEvent(LoginViewEvent.showErrorMessage)
        }
    }

    companion object {
        var initialEvent = LoginViewEvent.showLoginForm
    }
}